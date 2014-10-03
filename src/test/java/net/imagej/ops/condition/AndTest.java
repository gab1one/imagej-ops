package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;
import net.imagej.ops.AbstractOpTest;

import org.junit.Test;
 
public class AndTest extends AbstractOpTest {
 
    @Test
    public void testAnd()
    {
    	Condition<?> c1 =  (Condition<?>) ops.op(FunctionGreaterCondition.class, Double.class, 3.0);
        Condition<?> c2 =  (Condition<?>) ops.op(FunctionLesserCondition.class, Double.class, 6.0);

        Boolean result = (Boolean) ops.run(AndCondition.class, 5.0, c1,c2);        
        assertSame(result, true);
         
        Boolean result2 = (Boolean) ops.run(AndCondition.class, 2.0, c1,c2);        
        assertSame(result2, false);
        
        Boolean result3 = (Boolean) ops.run(AndCondition.class, 7.0, c1,c2);        
        assertSame(result3, false);
        
        Boolean result4 = (Boolean) ops.run(AndCondition.class, Double.NaN, c1,c2);        
        assertSame(result4, false);
    }
}