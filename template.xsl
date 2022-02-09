<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21.0cm" margin-top="1cm" margin-left="2cm" margin-right="2cm" margin-bottom="1cm">
          <!-- Page template goes here -->
          <fo:region-before region-name="xsl-region-before" extent="3cm"/>
          <fo:region-after region-name="xsl-region-after" extent="4cm"/>
          <fo:region-body />
        </fo:simple-page-master>
      </fo:layout-master-set>

      <fo:page-sequence master-reference="A4">
        <!-- Page content goes here -->
        <fo:static-content flow-name="xsl-region-before">
          <fo:block>
            <fo:table>
              <fo:table-column column-width="8.5cm"/>
              <fo:table-column column-width="8.5cm"/>
              <fo:table-body>
                <fo:table-row font-size="18pt" line-height="30px" background-color="#3e73b9" color="white">
                  <fo:table-cell padding-left="5pt">
                    <fo:block>
                      Acme Incorporated
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                    <fo:block text-align="right">
                      INVOICE
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell padding-left="5pt" padding-top="5pt">
                    <fo:block>
                      c/o Looney tunes&#x2028;
                      Toontown
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>

                  </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:static-content>
   
        <fo:flow flow-name="xsl-region-body" line-height="20pt">
          <xsl:apply-templates />
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="item">
    <fo:block space-before="120pt" width="17cm" >
      <fo:table>
        <fo:table-column column-width="5.5cm"/>
        <fo:table-column column-width="5.5cm"/>
        <fo:table-column column-width="3cm"/>
        <fo:table-column column-width="3cm"/>
        <fo:table-body>
          <fo:table-row>
            
            <fo:table-cell>
              <fo:block font-size="16pt" font-family="Serif"
						color="black" font-weight="bold" space-after="5mm">
						Order Id:
						<xsl:value-of select="order_id" />
					</fo:block>
					<fo:block font-size="16pt" font-family="Serif" text-align="right"
						color="black" font-weight="bold" space-after="5mm">
					<fo:inline font-weight="bold">Invoice Date</fo:inline>&#x2028;
						<xsl:value-of select="datetime" />
					</fo:block>
            </fo:table-cell>
        
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:block>
    <fo:block space-before="35pt">
      <fo:table line-height="30px">
        <fo:table-column column-width="2cm"/>
        <fo:table-column column-width="8cm"/>
        <fo:table-column column-width="3.5cm"/>
        <fo:table-column column-width="3.5cm"/>
        <fo:table-header>
          <fo:table-row background-color="#f5f5f5" text-align="center" font-weight="bold">
            <fo:table-cell border="1px solid #b8b6b6">
              <fo:block>Id</fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6">
              <fo:block>Name</fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6">
              <fo:block>Quantity</fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6">
              <fo:block>Unit Price</fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-header>
        <fo:table-body>
          <xsl:apply-templates select="lineitems/lineitem"></xsl:apply-templates>
          
          <fo:table-row font-weight="bold">
            <fo:table-cell number-columns-spanned="3" text-align="right" padding-right="3pt">
              <fo:block>Total</fo:block>
            </fo:table-cell>
            <fo:table-cell  text-align="right" padding-right="3pt" background-color="#f5f5f5" border="1px solid #b8b6b6" >
              <fo:block>
                <xsl:value-of select="total" />
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:block>
  </xsl:template>
  
  <xsl:template match="item">
    <fo:table-row>
    <fo:table-cell border="1px solid #b8b6b6" text-align="center">
				<fo:block>
					<xsl:value-of select="id" />
				</fo:block>
			</fo:table-cell>
	<fo:table-cell border="1px solid #b8b6b6" padding-left="3pt">
        <fo:block>
          <xsl:value-of select="name"/>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border="1px solid #b8b6b6" text-align="center">
        <fo:block>
          <xsl:value-of select="quantity"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
        <fo:block>
          <xsl:value-of select="mrp"/>
        </fo:block>
      </fo:table-cell>
    
    </fo:table-row>

  </xsl:template>
  </xsl:stylesheet>