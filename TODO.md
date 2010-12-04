# TODO List

- Migrate validation/compatibility checking to translator
- Alter ProxyFactory to be a concrete class that generates an InvocationHandler based on ProxyConfiguration instance
- Alter AnnotationDrivenProxyFactory so that it overrides `createProxy(del, ifc, conf)`
    - it should do nothing more than collect the config and pass it on to the super class

## Implementation Notes (API)

The API for the translator has to be driven off what you can put in the annotations. So, you *cannot* provide it with
much information about the target site (i.e., the method signature you're trying to map to on the delegate class).

When you read the annotations (in the AnnotationDrivenProxyFactory), I _think_ all you should do is construct the
MethodSignatureTranslator objects for each one, setting any properties for overriden method name, additional parameters
and method or type level TypeConverter support (see the current ProxyConfigurationBuilder for hints).

    // Asset.java
    public class Asset implements IAsset {
        // ... etc
    }

    // IAsset.java

    public IAsset {
        String getName();
        String getDescription();
        String getVersion();

        <T> T get(final String attribute);
        <T> void set(final String attribute, T object);
    }

    // Platform.java

    @ProxyInterface(delegate = Asset.class)
    @ProxyTypeConverterFactory(provider=StringWrapperConverterTypeFactory.class)
    public interface Platform extends IAsset {
        @ProxyMethod(methodName="get")
        @ProxyArguments({"platform-id"})
        long getPlatformId();
    }

    // Test.java

    public class Test {

        private final ProxyFactory factory = new AnnotationDrivenProxyFactory();

        public static void main(String... argv) {
            final IAsset asset = loadAssetFromService(....);
            final Platform platform = factory.createProxy(asset, Platform.class);

            System.out.println("platform id = " + platform.getPlatformId());    // must produce long platform id
            System.out.println("platform name = " + platform.getName());        // must produce String platform name
        }
    }