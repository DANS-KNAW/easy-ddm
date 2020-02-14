MANUAL
======
[![Build Status](https://travis-ci.org/DANS-KNAW/easy-ddm.svg?branch=master)](https://travis-ci.org/DANS-KNAW/easy-ddm)
DANS Dataset Metadata library

SAX PARSER
----------

When using a SAX parser you have to assign a content handler to a
reader. Such a handler receives notifications of document events such
as start-element and end-element.

Our handler map knows which handlers to use for which elements.
The handler map makes its decisions based on the parameters received at
the start of an element.

Leafs in the family of handler classes know in which element of
the target object (type parameter T, Handler.getTarget method) to put
the received data. The generic handler uses the handler map to switch
control between handlers. When giving control to another handler, the
target object, handler map and other administration are passed on. To
make sure subclasses of the generic handler can’t disturb control
switching, the methods startElement and endElement are declared final.
These event notifications are handed down to subclasses with other
method names: initElement/initFirstElement and finishElement. The
method initFirstElement gives handlers of complex elements a chance to
cleanup from a previous cycle, for example by creating an empty sub
target.

The handler map uses the default constructor of the handlers. The
other (package-private) constructor of the generic handler assigns
itself to the reader of the SAX parser. AbstractCrosswalk.parse uses
the latter constructor to start the crosswalk. But first it validates
the XML document against the XSD (type parameter V) to reduce the
validations required by the handlers.

Currently neither the handler map nor the handlers have any
knowledge about the path in the XML tree.

![Crosswalk](crosswalk.png)

BUILDING FROM SOURCE
--------------------

Prerequisites:

* Java 8 or higher
* Maven 3.3.3 or higher
 
Steps:

        git clone https://github.com/DANS-KNAW/easy-ingest-flow
        cd easy-ingest-flow
        mvn install

Replace the question marks in the `mvn` options
`-Deasy.emd.version=???` and/or `-Deasy.schema.version=???`
to use locally compiled versions of [easy-schema] respectively [easy-emd].
You can find the available versions in `~/.m2/repository/nl/knaw/dans/easy/easy-schema`
respectively `~/.m2/repository/nl/knaw/dans/easy/emd`.

[easy-schema]: https://github.com/DANS-KNAW/easy-schema
[easy-emd]: https://github.com/DANS-KNAW/easy-emd