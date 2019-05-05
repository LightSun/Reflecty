package com.heaven7.java.reflecty.iota;

import com.heaven7.java.reflecty.MemberProxy;

/**
 * the basic type adapter which used to write and read message members.
 * see {@link MemberProxy#TYPE_STRING} and etc.
 * @param <Out> the output type, used to write
 * @param <In> the input type ,used to read
 * @author heaven7
 */
public abstract class BasicTypeAdapter<Out,In> extends TypeAdapter<Out,In> {

    private TypeAdapter<Out,In> mNameAdapter;

    /**
     * get the type adapter which is used as the name. this is often helpful for map type adapter. eg: json-map
     * @return the name type adapter.
     */
    public final TypeAdapter<Out,In> getNameTypeAdapter(){
        return mNameAdapter != null ? mNameAdapter : (mNameAdapter = onCreateNameTypeAdapter());
    }

    protected abstract TypeAdapter<Out,In> onCreateNameTypeAdapter();


}
