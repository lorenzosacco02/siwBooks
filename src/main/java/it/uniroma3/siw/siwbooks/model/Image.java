package it.uniroma3.siw.siwbooks.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.deepEquals(content, image.content);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }
}
