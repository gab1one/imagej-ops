package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class XorTest extends AbstractOpTest {
 
    @Test
    public void testXor()
    {
      	Condition<?> c1 =  (Condition<?>) ops.op(FunctionGreaterCondition.class, Double.class, 3.0);
        Condition<?> c2 =  (Condition<?>) ops.op(FunctionLessCondition.class, Double.class, 6.0);

        Boolean result = (Boolean) ops.run(XorCondition.class, 5.0, c1,c2);        
        assertSame(result, false);
         
        Boolean result2 = (Boolean) ops.run(XorCondition.class, 2.0, c1,c2);        
        assertSame(result2, true);
        
        Boolean result3 = (Boolean) ops.run(XorCondition.class, 7.0, c1,c2);        
        assertSame(result3, true);
        
        Boolean result4 = (Boolean) ops.run(XorCondition.class, Double.NaN, c1,c2);        
        assertSame(result4, true);
    }
}