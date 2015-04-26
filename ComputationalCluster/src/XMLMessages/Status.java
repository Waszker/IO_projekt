//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.16 at 05:00:16 PM CET 
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

import XMLMessages.Error.ErrorMessage;
import DebugTools.Logger;
import GenericCommonClasses.AbstractMessage;
import GenericCommonClasses.GenericProtocol;
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
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="Threads">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Thread" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="State">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;enumeration value="Idle"/>
 *                                   &lt;enumeration value="Busy"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="HowLong" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *                             &lt;element name="ProblemInstanceId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *                             &lt;element name="TaskId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *                             &lt;element name="ProblemType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
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
@XmlType(name = "", propOrder = { "id", "threads" })
@XmlRootElement(name = "Status")
public class Status extends AbstractMessage
{
	@XmlElement(name = "Id", required = true)
	@XmlSchemaType(name = "unsignedLong")
	protected BigInteger id;
	@XmlElement(name = "Threads", required = true)
	protected Status.Threads threads;

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
	 * Gets the value of the threads property.
	 * 
	 * @return possible object is {@link Status.Threads }
	 * 
	 */
	public Status.Threads getThreads()
	{
		return threads;
	}

	/**
	 * Sets the value of the threads property.
	 * 
	 * @param value
	 *            allowed object is {@link Status.Threads }
	 * 
	 */
	public void setThreads(Status.Threads value)
	{
		this.threads = value;
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
	 *         &lt;element name="Thread" maxOccurs="unbounded">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="State">
	 *                     &lt;simpleType>
	 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
	 *                         &lt;enumeration value="Idle"/>
	 *                         &lt;enumeration value="Busy"/>
	 *                       &lt;/restriction>
	 *                     &lt;/simpleType>
	 *                   &lt;/element>
	 *                   &lt;element name="HowLong" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
	 *                   &lt;element name="ProblemInstanceId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
	 *                   &lt;element name="TaskId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
	 *                   &lt;element name="ProblemType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
	@XmlType(name = "", propOrder = { "thread" })
	public static class Threads
	{

		@XmlElement(name = "Thread", required = true)
		protected List<Status.Threads.Thread> thread;

		/**
		 * Gets the value of the thread property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the thread property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getThread().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Status.Threads.Thread }
		 * 
		 * 
		 */
		public List<Status.Threads.Thread> getThread()
		{
			if (thread == null)
			{
				thread = new ArrayList<Status.Threads.Thread>();
			}
			return this.thread;
		}

		/**
		 * <p>
		 * Java class for anonymous complex type.
		 * 
		 * <p>
		 * The following schema fragment specifies the expected content
		 * contained within this class.
		 * 
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="State">
		 *           &lt;simpleType>
		 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
		 *               &lt;enumeration value="Idle"/>
		 *               &lt;enumeration value="Busy"/>
		 *             &lt;/restriction>
		 *           &lt;/simpleType>
		 *         &lt;/element>
		 *         &lt;element name="HowLong" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
		 *         &lt;element name="ProblemInstanceId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
		 *         &lt;element name="TaskId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
		 *         &lt;element name="ProblemType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
		 *       &lt;/sequence>
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 * 
		 * 
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "state", "howLong",
				"problemInstanceId", "taskId", "problemType" })
		public static class Thread
		{

			@XmlElement(name = "State", required = true)
			protected String state;
			@XmlElement(name = "HowLong")
			@XmlSchemaType(name = "unsignedLong")
			protected BigInteger howLong;
			@XmlElement(name = "ProblemInstanceId")
			@XmlSchemaType(name = "unsignedLong")
			protected BigInteger problemInstanceId;
			@XmlElement(name = "TaskId")
			@XmlSchemaType(name = "unsignedLong")
			protected BigInteger taskId;
			@XmlElement(name = "ProblemType")
			protected String problemType;

			/**
			 * Gets the value of the state property.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getState()
			{
				return state;
			}

			/**
			 * Sets the value of the state property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setState(String value)
			{
				this.state = value;
			}

			/**
			 * Gets the value of the howLong property.
			 * 
			 * @return possible object is {@link BigInteger }
			 * 
			 */
			public BigInteger getHowLong()
			{
				return howLong;
			}

			/**
			 * Sets the value of the howLong property.
			 * 
			 * @param value
			 *            allowed object is {@link BigInteger }
			 * 
			 */
			public void setHowLong(BigInteger value)
			{
				this.howLong = value;
			}

			/**
			 * Gets the value of the problemInstanceId property.
			 * 
			 * @return possible object is {@link BigInteger }
			 * 
			 */
			public BigInteger getProblemInstanceId()
			{
				return problemInstanceId;
			}

			/**
			 * Sets the value of the problemInstanceId property.
			 * 
			 * @param value
			 *            allowed object is {@link BigInteger }
			 * 
			 */
			public void setProblemInstanceId(BigInteger value)
			{
				this.problemInstanceId = value;
			}

			/**
			 * Gets the value of the taskId property.
			 * 
			 * @return possible object is {@link BigInteger }
			 * 
			 */
			public BigInteger getTaskId()
			{
				return taskId;
			}

			/**
			 * Sets the value of the taskId property.
			 * 
			 * @param value
			 *            allowed object is {@link BigInteger }
			 * 
			 */
			public void setTaskId(BigInteger value)
			{
				this.taskId = value;
			}

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

		}

	}

	@Override
	public String getString() throws JAXBException
	{
		return Parser.marshallMessage(this, Status.class);
	}

	@Override
	public MessageType getMessageType()
	{
		return MessageType.STATUS;
	}

	@Override
	protected void getMessageResponse(IServerProtocol serverProtocol,
			Socket socket, List<IMessage> delayedResponse) throws IOException
	{
		List<IMessage> responses = new ArrayList<>();

		switch (serverProtocol.getComponentTypeFromId(getId()))
		{
			case TaskManager:
			case ComputationalNode:
				serverProtocol.informAboutComponentStatusMessage(getId());
				responses.add(serverProtocol.getNoOperationMessage());
				responses.addAll(serverProtocol.getStatusResponseMessages(
						getId(), getFreeThreads()));
				break;

			case ComputationalServer:
				serverProtocol.informAboutComponentStatusMessage(getId());
				responses.addAll(serverProtocol
						.getBackupServerSynchronizationMessages());
				break;

			default:
				Logger.log("Component not registered\n");
				responses.add(new Error(ErrorMessage.UnknownSender, ""));
				break;
		}

		GenericProtocol.sendMessages(socket,
				responses.toArray(new IMessage[responses.size()]));
	}

	private int getFreeThreads()
	{
		int freeThreads = 0;

		for (Threads.Thread t : getThreads().thread)
			if (t.state.contentEquals("idle")) freeThreads++;

		return freeThreads;
	}

	@Override
	public BigInteger getProblemId()
	{
		return null;
	}
}
