<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Attribute used for table border -->
	<xsl:attribute-set name="tableBorder">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
	</xsl:attribute-set>
	<xsl:template match="sales_items">
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
							<fo:table-column column-width="100%" />

							<fo:table-body>
								<fo:table-row font-size="18pt" line-height="30px"
									background-color="#3e73b9" color="white">

									<fo:table-cell>
										<fo:block text-align="center" padding-right="6pt">
											Sales
											Report
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
									<fo:block padding-top="15pt" padding-bottom="20pt"
										font-size="10pt" font-family="Helvetica" color="black"
										font-weight="bold" space-after="5mm">
										Start Date:
										<xsl:value-of select="startDate" />
									</fo:block>

								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-top="15pt" padding-bottom="20pt"
										font-size="10pt" font-family="Helvetica" color="black"
										font-weight="bold" space-after="5mm" text-align="right">
										End Date:
										<xsl:value-of select="endDate" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

						</fo:table-body>
					</fo:table>


					<fo:block font-size="10pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate">
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="4cm" />
							<fo:table-header>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold">Brand</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold">Category</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block text-align="right" font-weight="bold">Quantity
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block text-align="right" font-weight="bold">Revenue
									</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="sales_item" />
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="sales_item">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="tableBorder">
				<fo:block>
					<xsl:value-of select="brand" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder">
				<fo:block>
					<xsl:value-of select="category" />
				</fo:block>
			</fo:table-cell>

			<fo:table-cell xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="right">
					<xsl:value-of select="quantity" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="right">
					<xsl:value-of select="revenue" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>