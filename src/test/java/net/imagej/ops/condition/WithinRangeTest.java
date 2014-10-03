package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class WithinRangeTest extends AbstractOpTest {
 
    @Test
    public void testWithinRange()
    {
        Boolean result = (Boolean) ops.run(WithinRangeCondition.class, 4.0, 5.0, 3.0);
        assertSame(result, true);
        
        Boolean result2 = (Boolean) ops.run(WithinRangeCondition.class, 5.0, 3.0, 2.0);
        assertSame(result2, false);
        
        Boolean result3 = (Boolean) ops.run(WithinRangeCondition.class, 5.0, 3.0, 6.0);
        assertSame(result3, false);
    }
}