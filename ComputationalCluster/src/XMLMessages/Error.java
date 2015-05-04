//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.30 at 11:24:39 PM CEST 
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
 *         &lt;element name="ErrorType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="UnknownSender"/>
 *               &lt;enumeration value="InvalidOperation"/>
 *               &lt;enumeration value="ExceptionOccured"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "errorType", "errorMessage" })
@XmlRootElement(name = "Error")
public class Error implements IMessage
{
	public enum ErrorMessage
	{
		UnknownSender, InvalidOperation, ExceptionOccured
	}

	@XmlElement(name = "ErrorType", required = true)
	protected String errorType;
	@XmlElement(name = "ErrorMessage")
	protected String errorMessage;

	/**
	 * <p>
	 * Creates empty Error message.
	 * </p>
	 */
	public Error()
	{

	}

	/**
	 * <p>
	 * Creates error message with specified type and content.
	 * </p>
	 * 
	 * @param errorType
	 * @param errorMessage
	 */
	public Error(ErrorMessage errorType, String errorMessage)
	{
		setErrorType(errorType);
		setErrorMessage(errorMessage);
	}

	/**
	 * Gets the value of the errorType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getErrorType()
	{
		return errorType;
	}

	/**
	 * Sets the value of the errorType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setErrorType(ErrorMessage value)
	{
		switch (value)
		{
		case UnknownSender:
			this.errorType = "UnknownSender";
			break;

		case InvalidOperation:
			this.errorType = "InvalidOperation";
			break;

		case ExceptionOccured:
			this.errorType = "ExceptionOccured";
			break;
		}
	}

	/**
	 * Gets the value of the errorMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * Sets the value of the errorMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setErrorMessage(String value)
	{
		this.errorMessage = value;
	}

	@Override
	public String getString() throws JAXBException
	{
		return Parser.marshallMessage(this, Error.class);
	}

	@Override
	public MessageType getMessageType()
	{
		return MessageType.ERROR;
	}

	@Override
	public List<IMessage> prepareResponse(IServerProtocol serverProtocol,
			Socket socket)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getProblemId()
	{
		return null;
	}

}
