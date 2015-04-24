//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.16 at 05:00:12 PM CET 
//

package XMLMessages;

import java.math.BigInteger;
import java.net.Socket;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import GenericCommonClasses.IMessage;
import GenericCommonClasses.IServerProtocol;
import GenericCommonClasses.Parser;
import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProblemType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="ComputationalNodes" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="NodeID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "problemType", "id", "data",
		"computationalNodes", "nodeID" })
@XmlRootElement(name = "DivideProblem")
public class DivideProblem implements IMessage
{

	@XmlElement(name = "ProblemType", required = true)
	protected String problemType;
	@XmlElement(name = "Id", required = true)
	@XmlSchemaType(name = "unsignedLong")
	protected BigInteger id;
	@XmlElement(name = "Data", required = true)
	protected byte[] data;
	@XmlElement(name = "ComputationalNodes", required = true)
	@XmlSchemaType(name = "unsignedLong")
	protected BigInteger computationalNodes;
	@XmlElement(name = "NodeID", required = true)
	@XmlSchemaType(name = "unsignedLong")
	protected BigInteger nodeID;

	/**
	 * Gets the value of the problemType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProblemType()
	{
		return problemType;
	}

	/**
	 * Sets the value of the problemType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProblemType(String value)
	{
		this.problemType = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getId()
	{
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setId(BigInteger value)
	{
		this.id = value;
	}

	/**
	 * Gets the value of the data property.
	 * 
	 * @return possible object is byte[]
	 */
	public byte[] getData()
	{
		return data;
	}

	/**
	 * Sets the value of the data property.
	 * 
	 * @param value
	 *            allowed object is byte[]
	 */
	public void setData(byte[] value)
	{
		this.data = value;
	}

	/**
	 * Gets the value of the computationalNodes property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getComputationalNodes()
	{
		return computationalNodes;
	}

	/**
	 * Sets the value of the computationalNodes property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setComputationalNodes(BigInteger value)
	{
		this.computationalNodes = value;
	}

	/**
	 * Gets the value of the nodeID property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNodeID()
	{
		return nodeID;
	}

	/**
	 * Sets the value of the nodeID property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNodeID(BigInteger value)
	{
		this.nodeID = value;
	}

	@Override
	public String getString() throws JAXBException
	{
		return Parser.marshallMessage(this, DivideProblem.class);
	}

	@Override
	public MessageType getMessageType()
	{
		return MessageType.DIVIDE_PROBLEM;
	}

	@Override
	public List<IMessage> prepareResponse(IServerProtocol serverProtocol,
			Socket socket)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
