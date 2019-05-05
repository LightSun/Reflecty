package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.*;
import com.heaven7.java.reflecty.gson.member.StartEndMemberProxy;
import com.heaven7.java.reflecty.iota.ITypeAdapterManager;
import com.heaven7.java.reflecty.iota.TypeAdapter;

import java.io.IOException;
import java.util.List;

import static com.heaven7.java.reflecty.utils.IotaUtils.getTypeAdapter;

public class ObjectTypeAdapter extends GsonAdapter {

    private static final Reflecty<TypeAdapter<JsonWriter, JsonReader>,JsonAdapter, SerializedName, Deprecated, Expose> sReflecty;

    static {
        sReflecty = new ReflectyBuilder<TypeAdapter<JsonWriter, JsonReader>,JsonAdapter, SerializedName, Deprecated, Expose>()
                .classAnnotation(JsonAdapter.class)
                .fieldAnnotation(SerializedName.class)
                .methodAnnotation(null)
                .inheritAnnotation(Expose.class)
                .delegate(new GsonReflectyDelegate())
                .build();
    }

    private final ITypeAdapterManager<JsonWriter, JsonReader> mTAM;
    private final Class<?> mClazz;
    private final float mApplyVersion;

    public ObjectTypeAdapter(ITypeAdapterManager<JsonWriter, JsonReader> mTAM, Class<?> mClazz, float mApplyVersion) {
        this.mTAM = mTAM;
        this.mApplyVersion = mApplyVersion;
        this.mClazz = mClazz;
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        //dynamic first moved to TypeNode
        //annotation second
        TypeAdapter<JsonWriter, JsonReader> ta = sReflecty.performReflectClass(mClazz);
        if(ta != null){
            ta.write(sink, obj);
        }else {
            //last is use member
            List<MemberProxy> proxies = sReflecty.getMemberProxies(mClazz);
            sink.beginObject();
            try {
                for (MemberProxy proxy : proxies){
                    if(((StartEndMemberProxy)proxy).isVersionMatched(mApplyVersion)){
                        sink.name(proxy.getPropertyName());
                        getTypeAdapter(proxy.getTypeNode(), mTAM, mApplyVersion).write(sink, proxy.getValue(obj));
                    }
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            sink.endObject();
        }
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        //1, dynamic first moved to TypeNode
        //2, runtime annotation
        TypeAdapter<JsonWriter, JsonReader> ta = sReflecty.performReflectClass(mClazz);
        if(ta != null){
            return ta.read(source);
        }

        Object obj = mTAM.getReflectyContext().newInstance(mClazz);
        List<MemberProxy> proxies = sReflecty.getMemberProxies(mClazz);
        source.beginObject();
        try {
            while (source.hasNext()){
                MemberProxy proxy = findMemberProxy(proxies, source.nextName());
                if(proxy != null && ((StartEndMemberProxy) proxy).isVersionMatched(mApplyVersion)){
                    Object value = getTypeAdapter(proxy.getTypeNode(), mTAM, mApplyVersion).read(source);
                    proxy.setValue(obj, value);
                }else {
                    source.skipValue();
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        source.endObject();
        return obj;
    }

    private static MemberProxy findMemberProxy(List<MemberProxy> proxies, String name) {
        for (MemberProxy proxy : proxies){
            if(name.equals(proxy.getPropertyName())){
                return proxy;
            }
        }
        return null;
    }
}
