/**
 * Copyright (C) 2014 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.pf.language.ddm.api;

import java.io.File;

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.binding.EasyMetadataFactory;
import nl.knaw.dans.pf.language.emd.binding.EmdMarshaller;
import nl.knaw.dans.pf.language.emd.types.ApplicationSpecific.MetadataFormat;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkException;
import nl.knaw.dans.pf.language.xml.crosswalk.Crosswalker;
import nl.knaw.dans.pf.language.xml.exc.XMLSerializationException;
import nl.knaw.dans.pf.language.xml.validation.AbstractValidator2;
import nl.knaw.dans.pf.language.xml.validation.XMLErrorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Ddm2EmdCrosswalk extends Crosswalker<EasyMetadata> {
    private static final Logger logger = LoggerFactory.getLogger(Ddm2EmdCrosswalk.class);

    private AbstractValidator2 ddmValidator;

    /** Creates an instance. */
    public Ddm2EmdCrosswalk() {
        super(Ddm2EmdHandlerMap.getInstance());
        this.ddmValidator = new DDMValidator();
    }

    /**
     * Creates an instance with a non-default validator.
     * 
     * @param ddmValidator
     *        The validator to use
     */
    public Ddm2EmdCrosswalk(AbstractValidator2 ddmValidator) {
        super(Ddm2EmdHandlerMap.getInstance());
        this.ddmValidator = ddmValidator;
    }

    /**
     * Creates an object after validation against an XSD.
     * 
     * @param file
     *        with XML content
     * @return null if errors are reported by the {@link XMLErrorHandler}
     * @throws CrosswalkException
     */
    public EasyMetadata createFrom(final File file) throws CrosswalkException {
        return validateEMD(walk(ddmValidator, file, newTarget()));
    }

    /**
     * Creates an object assuming validation against an XSD has been done.
     * 
     * @param file
     *        with XML content
     * @return null if errors are reported by the {@link XMLErrorHandler}
     * @throws CrosswalkException
     */
    public EasyMetadata createFromValidated(final File file) throws CrosswalkException {
        return validateEMD(walk(null, file, newTarget()));
    }

    /**
     * Creates an object after validation against an XSD.
     * 
     * @param xml
     *        the XML content
     * @return null if errors are reported by the {@link XMLErrorHandler}
     * @throws CrosswalkException
     */
    public EasyMetadata createFrom(final String xml) throws CrosswalkException {
        return validateEMD(walk(ddmValidator, replaceSurrogatePairsWithTempCode(xml), newTarget()));
    }

    private String replaceSurrogatePairsWithTempCode(String xmlStr) {
        char[] chars = xmlStr.toCharArray();
        StringBuilder sb = new StringBuilder(chars.length);

        for (int i = 0; i < chars.length; ++i) {
            if (Character.isHighSurrogate(chars[i])) {
                if (i + 1 == chars.length)
                    throw new IllegalStateException("String contains high surrogate pair at the end");
                if (!Character.isSurrogatePair(chars[i], chars[i + 1]))
                    throw new IllegalStateException("Invalid surrogate pair at position " + i);
                sb.append("@@@SURROGATE-PAIR:").append(Character.codePointAt(chars, i)).append("@@@");
                i++;
            } else sb.append(chars[i]);
        }
        return sb.toString();
    }

    /**
     * Creates an object assuming validation against an XSD has been done.
     * 
     * @param xml
     *        the XML content
     * @return null if errors are reported by the {@link XMLErrorHandler}
     * @throws CrosswalkException
     */
    public EasyMetadata createFromValidated(final String xml) throws CrosswalkException {
        return validateEMD(walk(null, xml, newTarget()));
    }

    private EasyMetadata newTarget() {
        return EasyMetadataFactory.newEasyMetadata(MetadataFormat.DEFAULT);
    }

    private EasyMetadata validateEMD(final EasyMetadata emd) throws CrosswalkException {
        if (getXmlErrorHandler().getErrors().size() > 0 || getXmlErrorHandler().getFatalErrors().size() > 0)
            return null;
        try {
            // incomplete fields may cause trouble
            final String validatedXML = new EmdMarshaller(emd).getXmlString();
            logger.debug(validatedXML);
            return emd;
        }
        catch (final XMLSerializationException e) {
            String msg = "resulting Easy Meta Data is invalid: ";
            logger.error(msg, e);
            try {
                getXmlErrorHandler().error(new SAXParseException(msg + e.getMessage(), null));
            }
            catch (SAXException dummy) {
                // wrap the original exception, do not re-wrap
                throw new CrosswalkException(msg + e.getMessage(), e);
            }
            throw new CrosswalkException(msg + e.getMessage(), e);
        }
    }
}
