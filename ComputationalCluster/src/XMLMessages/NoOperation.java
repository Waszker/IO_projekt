//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.16 at 05:00:13 PM CET 
//


package XMLMessages;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import GenericCommonClasses.IMessage;
import GenericCommonClasses.IServerProtocol;
import GenericCommonClasses.Parser;
import GenericCommonClasses.Parser.MessageType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BackupCommunicationServers">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BackupCommunicationServer" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                           &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "backupCommunicationServers"
})
@XmlRootElement(name = "NoOperation")
public class NoOperation implements IMessage {

    @XmlElement(name = "BackupCommunicationServers", required = true)
    protected NoOperation.BackupCommunicationServers backupCommunicationServers;

    /**
     * Gets the value of the backupCommunicationServers property.
     * 
     * @return
     *     possible object is
     *     {@link NoOperation.BackupCommunicationServers }
     *     
     */
    public NoOperation.BackupCommunicationServers getBackupCommunicationServers() {
        return backupCommunicationServers;
    }

    /**
     * Sets the value of the backupCommunicationServers property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoOperation.BackupCommunicationServers }
     *     
     */
    public void setBackupCommunicationServers(NoOperation.BackupCommunicationServers value) {
        this.backupCommunicationServers = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BackupCommunicationServer" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *                 &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "backupCommunicationServer"
    })
    public static class BackupCommunicationServers {

        @XmlElement(name = "BackupCommunicationServer")
        protected NoOperation.BackupCommunicationServers.BackupCommunicationServer backupCommunicationServer;

        /**
         * Gets the value of the backupCommunicationServer property.
         * 
         * @return
         *     possible object is
         *     {@link NoOperation.BackupCommunicationServers.BackupCommunicationServer }
         *     
         */
        public NoOperation.BackupCommunicationServers.BackupCommunicationServer getBackupCommunicationServer() {
            return backupCommunicationServer;
        }

        /**
         * Sets the value of the backupCommunicationServer property.
         * 
         * @param value
         *     allowed object is
         *     {@link NoOperation.BackupCommunicationServers.BackupCommunicationServer }
         *     
         */
        public void setBackupCommunicationServer(NoOperation.BackupCommunicationServers.BackupCommunicationServer value) {
            this.backupCommunicationServer = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
         *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class BackupCommunicationServer {

            @XmlAttribute(name = "address")
            @XmlSchemaType(name = "anyURI")
            protected String address;
            @XmlAttribute(name = "port")
            @XmlSchemaType(name = "unsignedShort")
            protected Integer port;

            /**
             * Gets the value of the address property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAddress() {
                return address;
            }

            /**
             * Sets the value of the address property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAddress(String value) {
                this.address = value;
            }

            /**
             * Gets the value of the port property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getPort() {
                return port;
            }

            /**
             * Sets the value of the port property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setPort(Integer value) {
                this.port = value;
            }

        }

    }


	@Override
	public String getString() throws JAXBException
	{
		return Parser.marshallMessage(this, NoOperation.class);
	}

	@Override
	public MessageType getMessageType()
	{
		return MessageType.NO_OPERATION;
	}

	@Override
	public void prepareResponse(IServerProtocol serverProtocol,
			List<IMessage> quickResponses, List<IMessage> delayedResponses)
	{
		// TODO Auto-generated method stub
		
	}

}
