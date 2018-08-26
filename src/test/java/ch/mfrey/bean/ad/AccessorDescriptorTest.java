package ch.mfrey.bean.ad;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.mfrey.bean.ad.AccessorDescriptorBuilder.AccessorDescriptorBuilderListener;

public class AccessorDescriptorTest {
    private static final String ONBUILD = "ONBUILD";
    private static final Logger log = LoggerFactory.getLogger(AccessorDescriptorTest.class);

    @Test
    public void test() {
        AccessorDescriptorFactory factory = new AccessorDescriptorFactory();
        factory.configureBuilderListener(new AccessorDescriptorBuilderListener() {

            @Override
            public AccessorDescriptorBuilder onBuild(AccessorDescriptorBuilder builder) {
                if (builder.getAccessorContext().getAttribute(ONBUILD) == null) {
                    builder.getAccessorContext().setAttribute(ONBUILD, 0);
                } else {
                    builder.getAccessorContext().setAttribute(ONBUILD,
                            (int) builder.getAccessorContext().getAttribute(ONBUILD) + 1);
                }
                return builder;
            }

        });

        List<AccessorDescriptor> accessorDescriptors = factory.getAccessorDescriptors(C.class);
        accessorDescriptors.forEach(ad -> {
            log.info("{} {}",
                    ad.getAccessorContext().getAttribute(ONBUILD), ad);
            Assert.assertEquals(ad.getPropertyLevel(), (int) ad.getAccessorContext().getAttribute(ONBUILD));
        });

        accessorDescriptors = factory.getAccessorDescriptors(B.class);
        accessorDescriptors.forEach(ad -> {
            log.info("{} {}",
                    ad.getAccessorContext().getAttribute(ONBUILD), ad);
            Assert.assertEquals(ad.getPropertyLevel(), (int) ad.getAccessorContext().getAttribute(ONBUILD));
        });

        accessorDescriptors = factory.getAccessorDescriptors(A.class);
        accessorDescriptors.forEach(ad -> {
            log.info("{} {}",
                    ad.getAccessorContext().getAttribute(ONBUILD), ad);
            Assert.assertEquals(ad.getPropertyLevel(), (int) ad.getAccessorContext().getAttribute(ONBUILD));
        });
    }

}
