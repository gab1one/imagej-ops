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
         
        Context ctx = new Context();
        OpService op = ctx.service(OpService.class);
         
        Boolean result = (Boolean) (op).run("and", 5.0, 4.0);
        assertSame(result, false);
         
        Boolean result1 = (Boolean) (op).run("and", 5.0, 5.0);
        assertSame(result1, true);
    }
}