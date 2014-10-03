package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class NotTest extends AbstractOpTest {
 
    @Test
    public void testAnd()
    {
         
    	Condition<?> c1 =  (Condition<?>) ops.op(FunctionGreaterCondition.class, Double.class, 3.0);
        

        Boolean result = (Boolean) ops.run(NotCondition.class, 5.0, c1);        
        assertSame(result, false);
         
       
    	
        Boolean result2 = (Boolean) ops.run(NotCondition.class, 2.0, c1);        
        assertSame(result2, true);
     
    }
}