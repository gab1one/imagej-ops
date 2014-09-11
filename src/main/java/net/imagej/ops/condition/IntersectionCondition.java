package net.imagej.ops.condition;
 
import java.awt.Dimension;
import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
 
import net.imagej.ops.Function;
import net.imagej.ops.Op;
 
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
 
@Plugin(type = Op.class, name = "union")
public class IntersectionCondition<T> extends AbstractCondition<Object>  {
     
     
 
    @Parameter
    ArrayList<Condition<T>> conditions;
     
     
    @Override
    public boolean isTrue(Object val) {
         
         
         
        for(Condition<T> c1: conditions)
        {
            if(!c1.isTrue((T) val))
            {
                return false;
            }
        }
        return true;
         
    }
 
}