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
package nl.knaw.dans.pf.language.ddm.handlertypes;

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.InvalidDateStringException;
import nl.knaw.dans.pf.language.emd.types.IsoDate;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;

import org.joda.time.DateTime;
import org.xml.sax.SAXException;

public abstract class IsoDateHandler extends CrosswalkHandler<EasyMetadata> {
    /** @return now with just day precision */
    protected static DateTime getToday() {
        return new DateTime(new DateTime().toString("YYYY-MM-dd"));
    }

    protected void validateRange(final IsoDate isoDate, final DateTime min, final DateTime max) throws SAXException {
        if (isoDate.getValue().isBefore(min) || isoDate.getValue().isAfter(max))
            error("value out of range (" + min + ", " + max + ")");
    }

    protected IsoDate createDate(final String uri, final String localName) throws SAXException {
        final String value = getCharsSinceStart().trim();
        if (value.length() == 0)
            return null;
        final IsoDate isoDate = new IsoDate();
        if (value != null && value.trim().length() > 0) {
            try {
                isoDate.setValueAsString(value.trim());
            }
            catch (final InvalidDateStringException e) {
                error(e.getMessage());
            }
        }
        return isoDate;
    }
}
