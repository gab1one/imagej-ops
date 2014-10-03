package net.imagej.ops.condition;
 
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class OrTest extends AbstractOpTest {
 
    @Test
    public void testOr()
    {
         
    	Condition<?> c1 =  (Condition<?>) ops.op(FunctionGreaterCondition.class, Double.class, 3.0);
        Condition<?> c2 =  (Condition<?>) ops.op(FunctionLessCondition.class, Double.class, 6.0);
    	Condition<?> c3 =  (Condition<?>) ops.op(EqualsCondition.class, Double.class, 13.0);

        Boolean result = (Boolean) ops.run(OrCondition.class, 5.0, c1,c2);        
        assertSame(result, true);
         
        Boolean result2 = (Boolean) ops.run(OrCondition.class, 2.0, c1,c2);        
        assertSame(result2, true);
        
        Boolean result3 = (Boolean) ops.run(OrCondition.class, 7.0, c1,c2);        
        assertSame(result3, true);
        
       
        Boolean result4 = (Boolean) ops.run(OrCondition.class, 2.0, c1,c3);        
        assertSame(result4, false);
    }
}