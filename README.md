## Configuration (Maven)

1. Add the dependency.

```
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-base</artifactId>
        <version>${openfx.version}</version>
    </dependency>
    <dependency>
        <groupId>org.project</groupId>
        <artifactId>lomfx</artifactId>
        <version>${lomfx.version}</version>
    </dependency>
</dependencies>
```

1. In maven-compiler-plugin add the following configuration.

```
<configuration>
    <source>17</source>
    <target>17</target>
    <fork>true</fork>
    <compilerArgs>
        <arg>-J--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED</arg>
        <arg>-J--add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
        <arg>-J--add-exports=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED</arg>
        <arg>-J--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED</arg>
        <arg>-J--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED</arg>
        <arg>-J--add-exports=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED</arg>
        <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED</arg>
    </compilerArgs>
    <annotationProcessorPaths>
        <path>
            <groupId>org.project</groupId>
            <artifactId>lomfx</artifactId>
            <version>${lomfx.version}</version>
        </path>
    </annotationProcessorPaths>
</configuration>
```

## Usage Example

Example.java

```
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import org.project.lomfx.annotations.FxValue;

@FxValue
public class Example {

    BooleanProperty booleanProperty;
    DoubleProperty doubleProperty;
    FloatProperty floatProperty;
    IntegerProperty integerProperty;
    LongProperty longProperty;
    ObjectProperty<String> objectProperty;

}
```

After compile

```
import javafx.beans.property.ObjectProperty;

public class Example {
    ObjectProperty<String> data;

    public Example() {
    }

    public ObjectProperty<String> dataProperty() {
        return this.data;
    }

    public String getData() {
        return (String)this.data.getValue();
    }

    public void setData(String value) {
        this.data.setValue(value);
    }
}
```
