package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class FunctionGreaterTest extends AbstractOpTest {
 
    @Test
    public void testFunctionGreater()
    {
        Boolean result = (Boolean) ops.run(FunctionGreaterCondition.class, 5.0, 3.0);
        assertSame(result, true);
        
        Boolean result2 = (Boolean) ops.run(FunctionGreaterCondition.class, 5.0, 6.0);
        assertSame(result2, false);
    }
}