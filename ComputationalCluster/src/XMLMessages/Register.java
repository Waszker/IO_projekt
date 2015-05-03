//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.16 at 05:00:14 PM CET 
//

package XMLMessages;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import GenericCommonClasses.AbstractMessage;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.IServerProtocol;
import GenericCommonClasses.Parser;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Error.ErrorMessage;

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
 *         &lt;element name="Type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TaskManager"/>
 *               &lt;enumeration value="ComputationalNode"/>
 *               &lt;enumeration value="CommunicationServer"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SolvableProblems">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ProblemName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ParallelThreads" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="Deregister" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "type", "solvableProblems",
		"parallelThreads", "deregister", "id" })
@XmlRootElement(name = "Register")
public class Register extends AbstractMessage
{

	@XmlElement(name = "Type", required = true)
	protected String type;
	@XmlElement(name = "SolvableProblems", required = true)
	protected Register.SolvableProblems solvableProblems;
	@XmlElement(name = "ParallelThreads")
	@XmlSchemaType(name = "unsignedByte")
	protected short parallelThreads;
	@XmlElement(name = "Deregister")
	protected Boolean deregister;
	@XmlElement(name = "Id")
	@XmlSchemaType(name = "unsignedLong")
	protected BigInteger id;

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value)
	{
		this.type = value;
	}

	/**
	 * Gets the value of the solvableProblems property.
	 * 
	 * @return possible object is {@link Register.SolvableProblems }
	 * 
	 */
	public Register.SolvableProblems getSolvableProblems()
	{
		return solvableProblems;
	}

	/**
	 * Sets the value of the solvableProblems property.
	 * 
	 * @param value
	 *            allowed object is {@link Register.SolvableProblems }
	 * 
	 */
	public void setSolvableProblems(Register.SolvableProblems value)
	{
		this.solvableProblems = value;
	}

	/**
	 * Gets the value of the parallelThreads property.
	 * 
	 */
	public short getParallelThreads()
	{
		return parallelThreads;
	}

	/**
	 * Sets the value of the parallelThreads property.
	 * 
	 */
	public void setParallelThreads(short value)
	{
		this.parallelThreads = value;
	}

	/**
	 * Gets the value of the deregister property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isDeregister()
	{
		return deregister;
	}

	/**
	 * Sets the value of the deregister property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setDeregister(Boolean value)
	{
		this.deregister = value;
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
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="ProblemName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "problemName" })
	public static class SolvableProblems
	{

		@XmlElement(name = "ProblemName")
		protected List<String> problemName;

		/**
		 * Gets the value of the problemName property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the problemName property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getProblemName().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link String }
		 * 
		 * 
		 */
		public List<String> getProblemName()
		{
			if (problemName == null)
			{
				problemName = new ArrayList<String>();
			}
			return this.problemName;
		}

	}

	@Override
	public String getString() throws JAXBException
	{
		return Parser.marshallMessage(this, Register.class);
	}

	@Override
	public MessageType getMessageType()
	{
		return MessageType.REGISTER;
	}

	@Override
	protected void getMessageResponse(IServerProtocol serverProtocol,
			Socket socket, List<IMessage> delayedResponse) throws IOException
	{
		IMessage response = new XMLMessages.Error(ErrorMessage.UnknownSender,
				"");

		if (null != type)
		{
			GenericComponent.ComponentType componentType = getComponentType(getType());

			BigInteger id = serverProtocol.registerComponent(getId(), (int)getParallelThreads(),
					(null != isDeregister() && isDeregister().booleanValue()),
					componentType,
					(null != getSolvableProblems() ? getSolvableProblems()
							.getProblemName() : new ArrayList<String>()),
					(null == socket ? 0 : socket.getPort()),
					(null == socket ? "" : socket.getInetAddress().toString()));

			// If component is invalid
			if (-1 == id.intValue()
					&& componentType != ComponentType.ComputationalServer)
			{
				((Error) response)
						.setErrorMessage("Component cannot be registered");
			}
			else
			// Get component RegisterResponse message
			{
				// Response for newly registered BS requires BS list to be null
				response = new RegisterResponse(id,
						serverProtocol.getServerTimeout(),
						(-1 != id.intValue() ? null : serverProtocol
								.getBackupServer()));

				// Add message for backup server
				if (null != socket
						&& componentType != ComponentType.ComputationalServer)
				{
					setDeregister(false);
					setId(id);
					serverProtocol.addBackupServerMessage(this);
				}
			}
		}

		if (null != socket) GenericProtocol.sendMessages(socket, response);
	}

	@Override
	public BigInteger getProblemId()
	{
		return null;
	}
}
