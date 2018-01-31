package ch.mfrey.bean.ad;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.mfrey.bean.ad.AccessorDescriptorBuilder.AccessorDescriptorBuilderListener;
import org.junit.Assert;

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

    public static class A {

        private String aStr;
        private B b;
        private B[] bArray;
        private List<B> bList;
        private Map<String, B> bMap;

        public String getaStr() {
            return aStr;
        }

        public B getB() {
            return b;
        }

        public B[] getbArray() {
            return bArray;
        }

        public List<B> getbList() {
            return bList;
        }

        public Map<String, B> getbMap() {
            return bMap;
        }

        public void setaStr(final String aStr) {
            this.aStr = aStr;
        }

        public void setB(final B b) {
            this.b = b;
        }

        public void setbArray(final B[] bArray) {
            this.bArray = bArray;
        }

        public void setbList(final List<B> bList) {
            this.bList = bList;
        }

        public void setbMap(final Map<String, B> bMap) {
            this.bMap = bMap;
        }
    }

    public static class B {

        private A a;
        private String bStr;
        private C c;
        private C[] cArray;
        private List<C> cList;
        private Map<String, C> cMap;

        public A getA() {
            return a;
        }

        public String getbStr() {
            return bStr;
        }

        public C getC() {
            return c;
        }

        public C[] getcArray() {
            return cArray;
        }

        public List<C> getcList() {
            return cList;
        }

        public Map<String, C> getcMap() {
            return cMap;
        }

        public void setA(final A a) {
            this.a = a;
        }

        public void setbStr(final String bStr) {
            this.bStr = bStr;
        }

        public void setC(final C c) {
            this.c = c;
        }

        public void setcArray(final C[] cArray) {
            this.cArray = cArray;
        }

        public void setcList(final List<C> cList) {
            this.cList = cList;
        }

        public void setcMap(final Map<String, C> cMap) {
            this.cMap = cMap;
        }
    }

    public static class C {
        private String cStr;

        public String getcStr() {
            return cStr;
        }

        public void setcStr(final String cStr) {
            this.cStr = cStr;
        }

    }
}
