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
package nl.knaw.dans.pf.language.ddm.api;

import static nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace.*;
import nl.knaw.dans.pf.language.xml.validation.AbstractValidator2;

/**
 * Utility class for validating Dans Dataset Metadata.
 */
public class DDMValidator extends AbstractValidator2 {
    public DDMValidator() {
        // default schemas for DDM (online)
        super(DDM.xsd, DCX_GML.xsd, NARCIS_TYPE.xsd, IDENTIFIER_TYPE.xsd, ABR.xsd);
    }
}
