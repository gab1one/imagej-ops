package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;
 
import org.junit.Test;
import org.scijava.Context;
 
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class BooleanTest extends AbstractOpTest {
 
    @Test
    public void testBoolean()
    {
         
         
        Boolean result = (Boolean) ops.run(BooleanCondition.class, true);
        assertSame(result, true);
        
        Boolean result1 = (Boolean) ops.run(BooleanCondition.class, false);
        assertSame(result1, false);
    }
}