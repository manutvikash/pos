<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Attribute used for table border -->
	<xsl:attribute-set name="tableBorder">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
	</xsl:attribute-set>
	<xsl:template match="items">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21.0cm" margin="1cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:table>
							<fo:table-column column-width="50%" />
							<fo:table-column column-width="50%" />
							<fo:table-body>
								<fo:table-row font-size="18pt" line-height="30px"
									background-color="#3e73b9" color="white">
									<fo:table-cell padding-left="6pt">
										<fo:block>
											POS Application
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="right" padding-right="6pt">
											Invoice
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>

					</fo:block>
					<fo:table>
						<fo:table-column column-width="50%" />
						<fo:table-column column-width="50%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block padding-top="15pt" padding-bottom="20pt" font-size="10pt"
										font-family="Helvetica" color="black" font-weight="bold"
										space-after="5mm">
										Order Id:
										<xsl:value-of select="order_id" />
									</fo:block>

								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-top="15pt" padding-bottom="20pt" font-size="10pt" font-family="Helvetica"
										color="black" font-weight="bold" space-after="5mm" text-align="right">
										Date and Time:
										<xsl:value-of select="datetime" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

						</fo:table-body>
					</fo:table>


					<fo:block font-size="10pt">
						<fo:table table-layout="fixed" width="80%"
							border-collapse="separate" margin-left="15pt">
							<fo:table-column column-width="2cm" />
							<fo:table-column column-width="6cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-header>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" text-align="center">Id</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" text-align="center">Name</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" text-align="center">Quantity</fo:block>
								</fo:table-cell>
									<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" text-align="right">Unit Price</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="item" />
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="16pt" font-family="Helvetica"
						color="black" font-weight="bold" padding-top="10pt" text-align="right" space-after="5mm">
						Total: Rs.
						<xsl:value-of select="total" />
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="item">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center">
					<xsl:value-of select="id" />
				</fo:block>
			</fo:table-cell>

			<fo:table-cell xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center">
					<xsl:value-of select="name" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder">
					<fo:block text-align="center">
					<xsl:value-of select="quantity" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder">
			
				<fo:block text-align="right">
					<xsl:value-of select="mrp" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>