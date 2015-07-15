package org.skywing.code.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

/**
 * 
 * @author skywing
 *
 */
public class GenericUDFNvl2 extends GenericUDF {
    private transient ObjectInspector[] argumentIOs;
    private transient GenericUDFUtils.ReturnObjectInspectorResolver returnOIResolver;
    
    @Override
    public Object evaluate(DeferredObject[] args) throws HiveException {
        return null;
    }

    @Override
    public String getDisplayString(String[] children) {
        return null;
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] args)
            throws UDFArgumentException {
        if (args.length != 3) {
            throw new UDFArgumentLengthException(
                    "UDF nvl2 accepts exactly three arguments but "
                            + args.length + " given.");
        }
        this.argumentIOs = args;
        this.returnOIResolver = new GenericUDFUtils.ReturnObjectInspectorResolver(true);
        if (!(this.returnOIResolver.update(args[1]) && this.returnOIResolver
                .update(args[2]))) {
            throw new UDFArgumentTypeException(
                    3, "The second and third arguments of funtion NVL2 should have the same type, but they are different: \""
                            + args[1].getTypeName()
                            + "\" and \""
                            + args[2].getTypeName() + "\".");
        }
        return this.returnOIResolver.get();
    }

}
