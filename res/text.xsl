<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/users">
        <xsl:value-of select="'&#9;&#9;Список пользователей&#13;&#13;'"/>
        <xsl:for-each select="user">
            <xsl:value-of
                    select="concat('• ',@surname, ' ', @name, '&#13;&#9; e-mail: ', email, '&#13;&#9; Роли:&#13;')"/>
            <xsl:for-each select="roles/role">
                <xsl:value-of select="concat('&#9;&#9;- ',., '&#13;')"/>
            </xsl:for-each>
            <xsl:value-of select="'&#9; Телефоны:&#13;'"/>
            <xsl:for-each select="phones/phone">
                <xsl:value-of select="concat('&#9;&#9;- ',., '&#13;')"/>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
