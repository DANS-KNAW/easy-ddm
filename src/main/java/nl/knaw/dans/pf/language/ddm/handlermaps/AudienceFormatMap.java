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
package nl.knaw.dans.pf.language.ddm.handlermaps;

import java.io.IOException;
import java.util.Properties;

import nl.knaw.dans.common.lang.ResourceLocator;
import nl.knaw.dans.common.lang.ResourceNotFoundException;
import nl.knaw.dans.pf.language.emd.types.ApplicationSpecific.MetadataFormat;
import nl.knaw.dans.pf.language.emd.types.BasicString;
import nl.knaw.dans.pf.language.emd.types.EmdConstants;

public class AudienceFormatMap {
    private static Properties properties;

    private static Properties getProps() throws IOException, ResourceNotFoundException {
        if (properties == null) {
            properties = new Properties();
            properties.load(ResourceLocator.getInputStream("format.properties"));
        }
        return properties;
    }

    public static MetadataFormat get(final BasicString audience) {
        try {
            if (EmdConstants.SCHEME_ID_DISCIPLINES.equals(audience.getSchemeId())) {
                final String property = getProps().getProperty(audience.getValue(), MetadataFormat.DEFAULT.name());
                return MetadataFormat.valueOf(property);
            }
        }
        catch (final Throwable e) {}
        return MetadataFormat.DEFAULT;
    }
}
