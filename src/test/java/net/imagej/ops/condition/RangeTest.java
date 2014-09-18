package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;
 
import org.junit.Test;
import org.scijava.Context;
 
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class RangeTest extends AbstractOpTest {
 
    @Test
    public void testRange()
    {
         
        Context ctx = new Context();
        OpService op = ctx.service(OpService.class);
         
        Boolean result = (Boolean) (op).run("range", 0, 20, 2);
        assertSame(result, true);
         
        Boolean result1 = (Boolean) (op).run("range", 20, 0, 2);
        assertSame(result1, false);
    }
}