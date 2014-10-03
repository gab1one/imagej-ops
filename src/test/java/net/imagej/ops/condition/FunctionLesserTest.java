package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class FunctionLesserTest extends AbstractOpTest {
 
    @Test
    public void testFunctionLesser()
    {
         
    	 Boolean result = (Boolean) ops.run(FunctionLesserCondition.class, 5.0, 3.0);
         assertSame(result, false);
         
         Boolean result2 = (Boolean) ops.run(FunctionLesserCondition.class, 5.0, 6.0);
         assertSame(result2, true);
    }
}