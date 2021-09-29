/*
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
package nl.knaw.dans.pf.language.xml.crosswalk;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface CrosswalkHandlerMap<T> {
    /**
     * Get another handler to take over parsing control at the start of the current element. For description of the arguments see
     * {@link CrosswalkHandler#startElement(String, String, String, Attributes)}
     * 
     * @param uri
     * @param localName
     * @param attributes
     * @return if null the current handler will continue to have parse control
     * @throws SAXException
     */
    public CrosswalkHandler<T> getHandler(final String uri, final String localName, final Attributes attributes) throws SAXException;

    /**
     * Notification of an element without a handler of its own. For description of the arguments see
     * {@link CrosswalkHandler#startElement(String, String, String, Attributes)}
     * 
     * @param uri
     * @param localName
     * @param attributes
     * @return true if a warning should be generated (the configuration is incomplete)
     * @throws SAXException
     */
    boolean reportMissingHandler(final String uri, final String localName, final Attributes attributes) throws SAXException;
}
