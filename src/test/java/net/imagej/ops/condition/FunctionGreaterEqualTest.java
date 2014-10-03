package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class FunctionGreaterEqualTest extends AbstractOpTest {
 
    @Test
    public void testFunctionGreater()
    {
        Boolean result = (Boolean) ops.run(FunctionGreaterEqualCondition.class, 5.0, 3.0);
        assertSame(result, true);
        
        Boolean result3 = (Boolean) ops.run(FunctionGreaterEqualCondition.class, 5.0, 5.0);
        assertSame(result3, true);
        
        Boolean result2 = (Boolean) ops.run(FunctionGreaterEqualCondition.class, 5.0, 6.0);
        assertSame(result2, false);
    }
}