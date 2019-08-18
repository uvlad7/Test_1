<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:variable name="apos">'</xsl:variable>
    <xsl:output method="html"/>
    <xsl:template match="/users">
        <html>
            <head>
                <meta charset="UTF-8"></meta>
                <title>Список пользователей</title>
                <style>
                    body {
                    background: #f4f0cb;
                    }
                    table {
                    color: #271d0c;
                    width: 95%;
                    font-size: 16px;
                    margin: 20px;
                    border: #271d0c 1px solid;
                    border-radius: 3px;
                    box-shadow: 0 1px 2px #5f461b;
                    }

                    th {
                    padding: 21px 25px 22px 25px;
                    background: #f4f0cb;
                    text-align: center
                    }

                    tr {
                    text-align: center;
                    padding-left: 20px;
                    }

                    td {
                    padding: 18px;
                    border-left: 1px solid #f4f0cb;
                    border-right: 1px solid #f4f0cb;
                    }

                    tr, th {
                    overflow-wrap: break-word;
                    word-wrap: break-word;
                    }

                    tr:nth-child(even) {
                    background-color: #f4f0cb
                    }

                    tr:nth-child(odd) {
                    background-color: #ded29e
                    }
                </style>
            </head>
            <body>
                <table>
                    <thead>
                        <tr>
                            <th scope="col">Фамилия</th>
                            <th scope="col">Имя</th>
                            <th scope="col">E-mail</th>
                            <th scope="col">Роли</th>
                            <th scope="col">Телефоны</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="user">
                            <tr>
                                <td>
                                    <xsl:value-of select="@surname"/>
                                </td>
                                <td>
                                    <xsl:value-of select="@name"/>
                                </td>
                                <td>
                                    <xsl:value-of select="email"/>
                                </td>
                                <td>
                                    <ul>
                                        <xsl:for-each select="roles/role">
                                            <li>
                                                <xsl:value-of select="."/>
                                            </li>
                                        </xsl:for-each>
                                    </ul>
                                </td>
                                <td>
                                    <ul>
                                        <xsl:for-each select="phones/phone">
                                            <li>
                                                <xsl:value-of select="."/>
                                            </li>
                                        </xsl:for-each>
                                    </ul>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
