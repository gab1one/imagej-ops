package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;
 
import org.junit.Test;
import org.scijava.Context;
 
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class EqualsTest extends AbstractOpTest {
 
    @Test
    public void AndTest()
    {
         
        Context ctx = new Context();
        OpService op = ctx.service(OpService.class);
         
        Boolean result = (Boolean) (op).run("equals", 5.0, 4.0);
        assertSame(result, false);
         
        Boolean result2 = (Boolean) (op).run("equals", 5.0, 5.0);
        assertSame(result, true);
    }
}