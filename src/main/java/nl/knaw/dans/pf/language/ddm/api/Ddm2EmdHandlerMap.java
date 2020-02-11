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

import nl.knaw.dans.pf.language.ddm.datehandlers.DcDateHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasAvailableHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasCreatedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasDateAccepteddHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasDateCopyrightedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasDateHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasDateSubmittedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasIssuedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasModiefiedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.EasValidHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsAvailableHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsCreatedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsDateAccepteddHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsDateCopyrightedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsDateSubmittedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsIssuedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsModiefiedHandler;
import nl.knaw.dans.pf.language.ddm.datehandlers.TermsValidHandler;
import nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace;
import nl.knaw.dans.pf.language.ddm.handlers.AccessRightsHandler;
import nl.knaw.dans.pf.language.ddm.handlers.AlternativeTitleHandler;
import nl.knaw.dans.pf.language.ddm.handlers.AudienceHandler;
import nl.knaw.dans.pf.language.ddm.handlers.ContributorDetailsHandler;
import nl.knaw.dans.pf.language.ddm.handlers.CreatorDetailsHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DaiContributorHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DaiCreatorHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DcCoverageHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DcFormatHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DcLanguageHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DcPublisherHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DcSourceHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DcTypeHandler;
import nl.knaw.dans.pf.language.ddm.handlers.DescriptionHandler;
import nl.knaw.dans.pf.language.ddm.handlers.EasSpatialHandler;
import nl.knaw.dans.pf.language.ddm.handlers.TermsLicenseHandler;
import nl.knaw.dans.pf.language.ddm.handlers.SimpleContributorHandler;
import nl.knaw.dans.pf.language.ddm.handlers.SimpleCreatorHandler;
import nl.knaw.dans.pf.language.ddm.handlers.SkippedFieldHandler;
import nl.knaw.dans.pf.language.ddm.handlers.SubjectHandler;
import nl.knaw.dans.pf.language.ddm.handlers.TermsRightsHolderHandler;
import nl.knaw.dans.pf.language.ddm.handlers.TermsSpatialHandler;
import nl.knaw.dans.pf.language.ddm.handlers.TermsSpatialIso3166Handler;
import nl.knaw.dans.pf.language.ddm.handlers.TermsTemporalHandler;
import nl.knaw.dans.pf.language.ddm.handlers.TitleHandler;
import nl.knaw.dans.pf.language.ddm.handlers.ArchisIdentifierHandler;
import nl.knaw.dans.pf.language.ddm.handlers.IdentifierHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.AbstractSpatialHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialBoxHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialMultiPolygonHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialPointHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialPolygonHandler;
import nl.knaw.dans.pf.language.ddm.handlertypes.BasicDateHandler;
import nl.knaw.dans.pf.language.ddm.handlertypes.BasicIdentifierHandler;
import nl.knaw.dans.pf.language.ddm.handlertypes.BasicStringHandler;
import nl.knaw.dans.pf.language.ddm.handlertypes.IsoDateHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DcRelationHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmConformsToHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmHasFormatHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmHasPartHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmHasVersionHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmIsFormatOfHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmIsPartOfHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmIsReferencedByHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmIsReplacedByHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmIsRequiredByHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmIsVersionOfHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmReferencesHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmRelationHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmReplacesHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.DdmRequiresHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsIsVersionOfHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsConformsToHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsHasFormatHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsHasPartHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsHasVersionHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsIsFormatOfHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsIsPartOfHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsIsReferencedByHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsIsReplacedByHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsIsRequiredByHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsReferencesHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsReplacesHandler;
import nl.knaw.dans.pf.language.ddm.relationhandlers.TermsRequiresHandler;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandlerMap;
import nl.knaw.dans.pf.language.xml.vocabulary.MapFromXSD;
import org.dom4j.DocumentException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Ddm2EmdHandlerMap implements CrosswalkHandlerMap<EasyMetadata> {
    private static final SkippedFieldHandler SKIPPED_FIELD_HANDLER = new SkippedFieldHandler(null);
    private static final CrosswalkHandler<EasyMetadata> NOT_YET_IMPLEMENTED = new SkippedFieldHandler("not yet configured/implemented");
    private static final Ddm2EmdHandlerMap INSTANCE = new Ddm2EmdHandlerMap();
    private static Map<String, String> uri2prefix = initNameSpaceMap();
    private static Map<String, CrosswalkHandler<EasyMetadata>> map;

    public static Ddm2EmdHandlerMap getInstance() {
        return INSTANCE;
    }

    private static Map<String, String> initNameSpaceMap() {
        final Map<String, String> map = new HashMap<String, String>();
        for (final NameSpace ns : NameSpace.values())
            map.put(ns.uri, ns.prefix);
        return map;
    }

    /** no instantiation for a singleton */
    private Ddm2EmdHandlerMap() {}

    /** TODO let test achieve this with mocking and make the class not public */
    public Set<String> getKeys() {
        return map.keySet();
    }

    /** lazy initialization */
    private Map<String, CrosswalkHandler<EasyMetadata>> getMap() throws SAXException {
        if (map == null) {
            // note that a recursive chain of handlers would require multiple instances of handlers
            map = new HashMap<String, CrosswalkHandler<EasyMetadata>>();

            // TODO by business ingester: dateAvalable in two year range, no PID no AIP-ID
            putAudienceHandlers();
            putAuthorHandlers();
            putDateHandlers();
            putRelationHandlers();
            putAboutHandlers();
            putMiscellaneousHandlers();

            // avoid missing handler warnings
            putHandledByChilds();
            putNotImplementedHandlers();
        }
        return map;
    }

    @Override
    public CrosswalkHandler<EasyMetadata> getHandler(final String uri, final String localName, final Attributes attributes) throws SAXException {
        final String key = toHandlerKey(uri, localName, attributes);
        return getMap().get(key);
    }

    @Override
    public boolean reportMissingHandler(final String uri, final String localName, final Attributes attributes) throws SAXException {
        final String key = toHandlerKey(uri, localName, attributes);
        return !getMap().containsKey(key);
    }

    /**
     * @param uri
     * @param localName
     * @param attributes
     * @return [type/ns:localName] where<br>
     *         ns = translated to an internal name space prefix<br>
     *         type = the local name of the type attribute
     * @throws SAXException
     *         if lazy initialization of the map fails
     */
    private String toHandlerKey(final String uri, final String localName, final Attributes attributes) throws SAXException {
        final String element = uri2prefix.get(uri) + ":" + localName;

        // fix name space prefix when the local names become ambiguous
        final String type = toLocalName(attributes.getValue(NameSpace.XSI.uri, "type"));

        return type + "/" + element;
    }

    private Map<String, String> loadVocabulary(final String xsd) throws SAXException {
        try {
            return new MapFromXSD(xsd).getEnum2appInfo();
        }
        catch (final DocumentException e) {
            throw new SAXException("could not load map [" + xsd + "] " + e.getMessage(), e);
        }
    }

    private void putAudienceHandlers() throws SAXException {
        final BasicStringHandler narcisHandler = new AudienceHandler(loadVocabulary(NameSpace.NARCIS_TYPE.xsd));
        map.put("/ddm:audience", narcisHandler);
        map.put("DisciplineType/dcterms:audience", narcisHandler);
        final BasicStringHandler audienceHandler = new AudienceHandler();
        map.put("/dcterms:audience", audienceHandler);
        map.put("/dcterms:educationLevel", audienceHandler);
    }

    private void putAuthorHandlers() {
        final BasicStringHandler simpleCreatorHandler = new SimpleCreatorHandler();
        final BasicStringHandler simpleContributorHandler = new SimpleContributorHandler();
        map.put("/dc:creator", simpleCreatorHandler);
        map.put("/dc:contributor", simpleContributorHandler);
        map.put("/dcterms:creator", simpleCreatorHandler);
        map.put("/dcterms:contributor", simpleContributorHandler);
        map.put("/dcterms:rightsHolder", new TermsRightsHolderHandler());
        map.put("/dcx-dai:contributorDetails", new ContributorDetailsHandler());
        map.put("/dcx-dai:creatorDetails", new CreatorDetailsHandler());
        map.put("/dcx-dai:creator", new DaiCreatorHandler());
        map.put("/dcx-dai:contributor", new DaiContributorHandler());
    }

    private void putRelationHandlers() {
        final BasicStringHandler dcRelationHandler = new DcRelationHandler();

        map.put("/dc:relation", dcRelationHandler);

        map.put("/dcterms:relation", dcRelationHandler);
        map.put("/dcterms:conformsTo", new TermsConformsToHandler());
        map.put("/dcterms:isVersionOf", new TermsIsVersionOfHandler());
        map.put("/dcterms:hasVersion", new TermsHasVersionHandler());
        map.put("/dcterms:isReplacedBy", new TermsIsReplacedByHandler());
        map.put("/dcterms:replaces", new TermsReplacesHandler());
        map.put("/dcterms:isRequiredBy", new TermsIsRequiredByHandler());
        map.put("/dcterms:requires", new TermsRequiresHandler());
        map.put("/dcterms:isPartOf", new TermsIsPartOfHandler());
        map.put("/dcterms:hasPart", new TermsHasPartHandler());
        map.put("/dcterms:isReferencedBy", new TermsIsReferencedByHandler());
        map.put("/dcterms:references", new TermsReferencesHandler());
        map.put("/dcterms:isFormatOf", new TermsIsFormatOfHandler());
        map.put("/dcterms:hasFormat", new TermsHasFormatHandler());

        map.put("ISBN/dcterms:relation", new DcRelationHandler("ISBN"));
        map.put("ISBN/dcterms:conformsTo", new TermsConformsToHandler("ISBN"));
        map.put("ISBN/dcterms:isVersionOf", new TermsIsVersionOfHandler("ISBN"));
        map.put("ISBN/dcterms:hasVersion", new TermsHasVersionHandler("ISBN"));
        map.put("ISBN/dcterms:isReplacedBy", new TermsIsReplacedByHandler("ISBN"));
        map.put("ISBN/dcterms:replaces", new TermsReplacesHandler("ISBN"));
        map.put("ISBN/dcterms:isRequiredBy", new TermsIsRequiredByHandler("ISBN"));
        map.put("ISBN/dcterms:requires", new TermsRequiresHandler("ISBN"));
        map.put("ISBN/dcterms:isPartOf", new TermsIsPartOfHandler("ISBN"));
        map.put("ISBN/dcterms:hasPart", new TermsHasPartHandler("ISBN"));
        map.put("ISBN/dcterms:isReferencedBy", new TermsIsReferencedByHandler("ISBN"));
        map.put("ISBN/dcterms:references", new TermsReferencesHandler("ISBN"));
        map.put("ISBN/dcterms:isFormatOf", new TermsIsFormatOfHandler("ISBN"));
        map.put("ISBN/dcterms:hasFormat", new TermsHasFormatHandler("ISBN"));

        map.put("ISSN/dcterms:relation", new DcRelationHandler("ISSN"));
        map.put("ISSN/dcterms:conformsTo", new TermsConformsToHandler("ISSN"));
        map.put("ISSN/dcterms:isVersionOf", new TermsIsVersionOfHandler("ISSN"));
        map.put("ISSN/dcterms:hasVersion", new TermsHasVersionHandler("ISSN"));
        map.put("ISSN/dcterms:isReplacedBy", new TermsIsReplacedByHandler("ISSN"));
        map.put("ISSN/dcterms:replaces", new TermsReplacesHandler("ISSN"));
        map.put("ISSN/dcterms:isRequiredBy", new TermsIsRequiredByHandler("ISSN"));
        map.put("ISSN/dcterms:requires", new TermsRequiresHandler("ISSN"));
        map.put("ISSN/dcterms:isPartOf", new TermsIsPartOfHandler("ISSN"));
        map.put("ISSN/dcterms:hasPart", new TermsHasPartHandler("ISSN"));
        map.put("ISSN/dcterms:isReferencedBy", new TermsIsReferencedByHandler("ISSN"));
        map.put("ISSN/dcterms:references", new TermsReferencesHandler("ISSN"));
        map.put("ISSN/dcterms:isFormatOf", new TermsIsFormatOfHandler("ISSN"));
        map.put("ISSN/dcterms:hasFormat", new TermsHasFormatHandler("ISSN"));

        map.put("NWO-PROJECTNR/dcterms:relation", new DcRelationHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:conformsTo", new TermsConformsToHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:isVersionOf", new TermsIsVersionOfHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:hasVersion", new TermsHasVersionHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:isReplacedBy", new TermsIsReplacedByHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:replaces", new TermsReplacesHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:isRequiredBy", new TermsIsRequiredByHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:requires", new TermsRequiresHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:isPartOf", new TermsIsPartOfHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:hasPart", new TermsHasPartHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:isReferencedBy", new TermsIsReferencedByHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:references", new TermsReferencesHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:isFormatOf", new TermsIsFormatOfHandler("NWO-PROJECTNR"));
        map.put("NWO-PROJECTNR/dcterms:hasFormat", new TermsHasFormatHandler("NWO-PROJECTNR"));

        map.put("/ddm:relation", new DdmRelationHandler());
        map.put("/ddm:conformsTo", new DdmConformsToHandler());
        map.put("/ddm:isVersionOf", new DdmIsVersionOfHandler());
        map.put("/ddm:hasVersion", new DdmHasVersionHandler());
        map.put("/ddm:isReplacedBy", new DdmIsReplacedByHandler());
        map.put("/ddm:replaces", new DdmReplacesHandler());
        map.put("/ddm:isRequiredBy", new DdmIsRequiredByHandler());
        map.put("/ddm:requires", new DdmRequiresHandler());
        map.put("/ddm:isPartOf", new DdmIsPartOfHandler());
        map.put("/ddm:hasPart", new DdmHasPartHandler());
        map.put("/ddm:isReferencedBy", new DdmIsReferencedByHandler());
        map.put("/ddm:references", new DdmReferencesHandler());
        map.put("/ddm:isFormatOf", new DdmIsFormatOfHandler());
        map.put("/ddm:hasFormat", new DdmHasFormatHandler());
    }

    private void putNotImplementedHandlers() {
        map.put("/dcterms:instructionalMethod", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:accrualMethod", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:accrualPolicy", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:accrualPeriodicity", SKIPPED_FIELD_HANDLER);

        map.put("/dcterms:mediator", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:provenance", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:bibliographicCitation", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:medium", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:extent", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:abstract", SKIPPED_FIELD_HANDLER);
        map.put("/dcterms:tableOfContents", SKIPPED_FIELD_HANDLER);
    }

    private void putDateHandlers() {
        // EasyMetadataImpl: EmdDate emdDate;
        final IsoDateHandler easCreatedHandler = new EasCreatedHandler();
        map.put("/ddm:created", easCreatedHandler);
        map.put("W3CDTF/dcterms:created", easCreatedHandler);
        map.put("/dcterms:created", new TermsCreatedHandler());

        final IsoDateHandler easAvailableHandler = new EasAvailableHandler();
        map.put("/ddm:available", easAvailableHandler);
        map.put("W3CDTF/dcterms:available", easAvailableHandler);
        map.put("/dcterms:available", new TermsAvailableHandler());

        map.put("W3CDTF/dcterms:valid", new EasValidHandler());
        map.put("/dcterms:valid", new TermsValidHandler());

        map.put("W3CDTF/dcterms:issued", new EasIssuedHandler());
        map.put("/dcterms:issued", new TermsIssuedHandler());

        map.put("W3CDTF/dcterms:modified", new EasModiefiedHandler());
        map.put("/dcterms:modified", new TermsModiefiedHandler());

        map.put("W3CDTF/dcterms:dateAccepted", new EasDateAccepteddHandler());
        map.put("/dcterms:dateAccepted", new TermsDateAccepteddHandler());

        map.put("W3CDTF/dcterms:dateCopyrighted", new EasDateCopyrightedHandler());
        map.put("/dcterms:dateCopyrighted", new TermsDateCopyrightedHandler());

        map.put("W3CDTF/dcterms:dateSubmitted", new EasDateSubmittedHandler());
        map.put("/dcterms:dateSubmitted", new TermsDateSubmittedHandler());

        final BasicDateHandler dcDateHandler = new DcDateHandler();
        map.put("/dc:date", dcDateHandler);
        map.put("/dcterms:date", dcDateHandler);
        final EasDateHandler easDateHandler = new EasDateHandler();
        map.put("W3CDTF/dc:date", easDateHandler);
        map.put("W3CDTF/dcterms:date", easDateHandler);
    }

    private void putHandledByChilds() {
        map.put("/dcx-dai:organization", null);
        map.put("/dcx-dai:author", null);
        map.put("/ddm:dcmiMetadata", null);
        map.put("/ddm:profile", null);
        map.put("/ddm:DDM", null);
        map.put("/ddm:additional-xml", null);
    }

    private void putMiscellaneousHandlers() {
        // 3-fold checks: maxDDM as generated by oXygen / EasyMetadataImpl fields / deposit pages
        // <ref-panelId> mainly from emd-view-definition in archaeology.xml and unspecified.xml

        map.put("/ddm:accessRights", new AccessRightsHandler());
        // TODO additional access rights not yet implemented
        map.put("/dcterms:accessRights", NOT_YET_IMPLEMENTED);
        map.put("/dc:rights", NOT_YET_IMPLEMENTED);
        map.put("/dcterms:rights", NOT_YET_IMPLEMENTED);
        map.put("URI/dcterms:license", new TermsLicenseHandler());
        // <ref-panelId>dc.rights</ref-panelId>
        // <ref-panelId>dcterms.accessrights</ref-panelId>
        // EasyMetadataImpl: EmdRights emdRights;

        // life science / archaeology
        final BasicStringHandler dcPublisherHandler = new DcPublisherHandler();
        map.put("/dc:publisher", dcPublisherHandler);
        map.put("/dcterms:publisher", dcPublisherHandler);
        // EasyMetadataImpl: EmdPublisher emdPublisher;

        final BasicStringHandler titleHandler = new TitleHandler();
        map.put("/dc:title", titleHandler);
        map.put("/dcterms:title", titleHandler);
        // EasyMetadataImpl: EmdTitle emdTitle;
        map.put("/dcterms:alternative", new AlternativeTitleHandler());
        // <ref-panelId>dcterms.alternative</ref-panelId>

        final BasicStringHandler descriptionHandler = new DescriptionHandler();
        map.put("/dc:description", descriptionHandler);
        map.put("/dcterms:description", descriptionHandler);
        map.put("/ddm:description", descriptionHandler);
        // EasyMetadataImpl: EmdDescription emdDescription;

        final BasicStringHandler dcFormatHandler = new DcFormatHandler(false);
        final BasicStringHandler imtFormatHandler = new DcFormatHandler(true);
        map.put("/dc:format", dcFormatHandler);
        map.put("/dcterms:format", dcFormatHandler);
        map.put("IMT/dc:format", imtFormatHandler);
        map.put("IMT/dcterms:format", imtFormatHandler);
        // <ref-panelId>dc.format.imt</ref-panelId>
        // <ref-panelId>dc.format</ref-panelId>
        // EasyMetadataImpl: EmdFormat emdFormat;

        // not provided by DDM
        // <ref-panelId>eas.remarks</ref-panelId>
        // EasyMetadataImpl: EmdOther emdOther;

        final BasicIdentifierHandler identifierHandler = new IdentifierHandler();
        final BasicIdentifierHandler isbnIdentifierHandler = new IdentifierHandler("ISBN");
        final BasicIdentifierHandler issnIdentifierHandler = new IdentifierHandler("ISSN");
        final BasicIdentifierHandler nwoIdentifierHandler = new IdentifierHandler("NWO-projectnummer");
        final BasicIdentifierHandler eDNAprojectIdentifierHandler = new IdentifierHandler("eDNA-project");
        final BasicIdentifierHandler archisIdentifierHandler = new ArchisIdentifierHandler();
        map.put("/dc:identifier", identifierHandler);
        map.put("/dcterms:identifier", identifierHandler);
        map.put("ISBN/dc:identifier", isbnIdentifierHandler);
        map.put("ISBN/dcterms:identifier", isbnIdentifierHandler);
        map.put("ISSN/dc:identifier", issnIdentifierHandler);
        map.put("ISSN/dcterms:identifier", issnIdentifierHandler);
        map.put("NWO-PROJECTNR/dc:identifier", nwoIdentifierHandler);
        map.put("NWO-PROJECTNR/dcterms:identifier", nwoIdentifierHandler);
        map.put("ARCHIS-ZAAK-IDENTIFICATIE/dc:identifier", archisIdentifierHandler);
        map.put("ARCHIS-ZAAK-IDENTIFICATIE/dcterms:identifier", archisIdentifierHandler);
        map.put("eDNA-project/dc:identifier", eDNAprojectIdentifierHandler);
        map.put("eDNA-project/dcterms:identifier", eDNAprojectIdentifierHandler);
        // <ref-panelId>dc.identifier</ref-panelId>
        // EasyMetadataImpl: EmdIdentifier emdIdentifier;
        // Not supported By DDM:
        // <ref-panelId>archis2.view</ref-panelId>
        // BasicIdentifier bi =
        // emd.getEmdIdentifier().getAllIdentfiers(EmdConstants.SCHEME_ARCHIS_ONDERZOEK_M_NR))

        final BasicStringHandler dcLanguageHandler = new DcLanguageHandler();
        map.put("/dc:language", dcLanguageHandler);
        map.put("/dcterms:language", dcLanguageHandler);
        map.put("ISO639-3/dc:language", dcLanguageHandler);
        map.put("ISO639-3/dcterms:language", dcLanguageHandler);
        map.put("ISO639-2/dc:language", dcLanguageHandler);
        map.put("ISO639-2/dcterms:language", dcLanguageHandler);
        // <ref-panelId>dc.language.iso639</ref-panelId>
        // <ref-panelId>dc.language</ref-panelId>
        // EasyMetadataImpl: EmdLanguage emdLanguage;

        final BasicIdentifierHandler dcSourceHandler = new DcSourceHandler();
        map.put("/dc:source", dcSourceHandler);
        map.put("/dcterms:source", dcSourceHandler);
        // <ref-panelId>dc.source</ref-panelId>
        // EasyMetadataImpl: EmdSource emdSource;

        final BasicStringHandler dcTypeHandler = new DcTypeHandler();
        map.put("/dc:type", dcTypeHandler);
        map.put("/dcterms:type", dcTypeHandler);
        map.put("DCMIType/dc:type", dcTypeHandler);
        map.put("DCMIType/dcterms:type", dcTypeHandler);
        // <ref-panelId>dc.type</ref-panelId>
        // EasyMetadataImpl: EmdType emdType;
    }

    private void putAboutHandlers() {
        final BasicStringHandler dcCoverageHandler = new DcCoverageHandler();
        map.put("/dc:coverage", dcCoverageHandler);
        map.put("/dcterms:coverage", dcCoverageHandler);
        // EasyMetadataImpl: EmdCoverage emdCoverage;

        Map<String, AbstractSpatialHandler> spatialSubHandlers = new HashMap<String, AbstractSpatialHandler>();

        final SpatialPointHandler spatialPointHandler = new SpatialPointHandler();
        spatialSubHandlers.put("Point", spatialPointHandler);

        final SpatialBoxHandler spatialBoxHandler = new SpatialBoxHandler();
        spatialSubHandlers.put("Envelope", spatialBoxHandler);

        final SpatialPolygonHandler polygonHandler = new SpatialPolygonHandler();
        spatialSubHandlers.put("Polygon", polygonHandler);

        final SpatialMultiPolygonHandler multiPolygonHandler = new SpatialMultiPolygonHandler(polygonHandler);
        spatialSubHandlers.put("MultiSurface", multiPolygonHandler);

        final EasSpatialHandler easSpatialHandler = new EasSpatialHandler(spatialSubHandlers);
        map.put("/dcterms:spatial", new TermsSpatialHandler());
        map.put("ISO3166/dcterms:spatial", new TermsSpatialIso3166Handler());
        map.put("/dcx-gml:spatial", easSpatialHandler);
        map.put("SimpleGMLType/dcterms:spatial", easSpatialHandler);
        map.put("/gml:Point", spatialPointHandler);
        map.put("/gml:boundedBy", spatialBoxHandler);
        map.put("/gml:Polygon", polygonHandler);
        map.put("/gml:MultiSurface", multiPolygonHandler);
        // <ref-panelId>dcterms.spatial</ref-panelId>
        // <ref-panelId>eas.spatial.point</ref-panelId>
        // <ref-panelId>eas.spatial.box</ref-panelId>
        // getEmdCoverage().get...

        final TermsTemporalHandler temporalHandler = new TermsTemporalHandler();
        map.put("/dcterms:temporal", temporalHandler);
        map.put("/ddm:temporal", temporalHandler);
        map.put("ABRperiode/dcterms:temporal", new TermsTemporalHandler(NameSpace.ABR));
        // <ref-panelId>dcterms.temporal</ref-panelId>
        // <ref-panelId>dcterms.temporal.abr</ref-panelId>
        // getEmdCoverage().get...

        final BasicStringHandler subjectHandler = new SubjectHandler();
        final BasicStringHandler abrSubjectHandler = new SubjectHandler(NameSpace.ABR);
        map.put("/dc:subject", subjectHandler);
        map.put("/dcterms:subject", subjectHandler);
        map.put("/ddm:subject", subjectHandler);
        map.put("ABRcomplex/dc:subject", abrSubjectHandler);
        map.put("ABRcomplex/dcterms:subject", abrSubjectHandler);
        // <ref-panelId>dc.subject.abr</ref-panelId>
        // EasyMetadataImpl: EmdSubject emdSubject;
    }

    private String toLocalName(final String value) {
        final String localName;
        if (value == null)
            localName = "";
        else if (value.contains(":"))
            localName = value.split(":")[1];
        else
            localName = value;
        return localName;
    }
}
