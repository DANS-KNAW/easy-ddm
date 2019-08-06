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
package nl.knaw.dans.pf.language.ddm.handlermaps;

public enum NameSpace {
    DC("dc", "http://purl.org/dc/elements/1.1/", "https://dublincore.org/schemas/xmls/qdc/2008/02/11/dc.xsd"), //
    DC_TERMS("dcterms", "http://purl.org/dc/terms/", "https://dublincore.org/schemas/xmls/qdc/2008/02/11/dcterms.xsd"), //
    DCMITYPE("dcmitype", "http://purl.org/dc/dcmitype/", "https://dublincore.org/schemas/xmls/qdc/2008/02/11/dcmitype.xsd"), //
    DCX("dcx", "http://easy.dans.knaw.nl/schemas/dcx/", "https://easy.dans.knaw.nl/schemas/dcx/2012/10/dcx.xsd"), //
    GML("gml", "http://www.opengis.net/gml", "http://schemas.opengis.net/gml/3.2.1/gml.xsd"), //
    DCX_DAI("dcx-dai", "http://easy.dans.knaw.nl/schemas/dcx/dai/", "https://easy.dans.knaw.nl/schemas/dcx/2019/01/dcx-dai.xsd"), //
    DCX_GML("dcx-gml", "http://easy.dans.knaw.nl/schemas/dcx/gml/", "https://easy.dans.knaw.nl/schemas/dcx/2016/dcx-gml.xsd"), //
    DDM("ddm", "http://easy.dans.knaw.nl/schemas/md/ddm/", "https://easy.dans.knaw.nl/schemas/md/2019/07/ddm.xsd"), //
    XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance", "https://www.w3.org/2001/XMLSchema-instance"), //
    NARCIS_TYPE("narcis", "http://easy.dans.knaw.nl/schemas/vocab/narcis-type/", "https://easy.dans.knaw.nl/schemas/vocab/2019/01/narcis-type.xsd"), //
    IDENTIFIER_TYPE("id-type", "http://easy.dans.knaw.nl/schemas/vocab/identifier-type/", "https://easy.dans.knaw.nl/schemas/vocab/2017/09/identifier-type.xsd"), //
    ABR("abr", "http://www.den.nl/standaard/166/Archeologisch-Basisregister/", "https://easy.dans.knaw.nl/schemas/vocab/2012/10/abr-type.xsd");

    public final String uri;
    public final String prefix;
    public final String xsd;

    private NameSpace(final String prefix, final String uri, final String xsd) {
        this.prefix = prefix;
        this.uri = uri;
        this.xsd = xsd;
    }
}
