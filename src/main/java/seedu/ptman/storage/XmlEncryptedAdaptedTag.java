package seedu.ptman.storage;

import static seedu.ptman.commons.encrypter.DataEncrypter.decrypt;
import static seedu.ptman.commons.encrypter.DataEncrypter.encrypt;

import javax.xml.bind.annotation.XmlValue;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.tag.Tag;

//@@author SunBangjie
/**
 * JAXB-friendly adapted version of the Tag.
 */
public class XmlEncryptedAdaptedTag {

    public static final String DECRYPT_FAIL_MESSAGE = "Cannot decrypt %s";

    @XmlValue
    private String tagName;

    /**
     * Constructs an XmlAdaptedTag.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlEncryptedAdaptedTag() {}

    /**
     * Constructs a {@code XmlAdaptedTag} with the given {@code tagName}.
     */
    public XmlEncryptedAdaptedTag(String tagName) {
        try {
            this.tagName = encrypt(tagName);
        } catch (Exception e) {
            //Encryption should not fail
        }
    }

    /**
     * Converts a given Tag into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlEncryptedAdaptedTag(Tag source) {
        try {
            tagName = encrypt(source.tagName);
        } catch (Exception e) {
            //Encryption should not fail
        }
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Tag object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted employee
     */
    public Tag toModelType() throws IllegalValueException {
        String decryptedTagName;
        try {
            decryptedTagName = decrypt(tagName);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Tag.class.getSimpleName()));
        }
        if (!Tag.isValidTagName(decryptedTagName)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(decryptedTagName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlEncryptedAdaptedTag)) {
            return false;
        }

        return tagName.equals(((XmlEncryptedAdaptedTag) other).tagName);
    }
}
