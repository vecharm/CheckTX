// ICheckTx.aidl
package org.moson.checktx;

// Declare any non-default types here with import statements

interface ICheckTx {

    void setPermission(String packagename,in String[] permissions);

    void setStackInfo(String packagename,String method, String stackInfo, boolean front);
}
