package nl.knaw.dans.pf.language.ddm.handlers.spatial;

// since we're not in Java 8 yet, I have to define my own consumer
public interface Consumer<T> {

    void accept(T t);
}
