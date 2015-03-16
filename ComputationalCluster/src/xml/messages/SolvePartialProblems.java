//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.16 at 05:00:13 PM CET 
//


package xml.messages;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="ProblemType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="CommonData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="SolvingTimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 *         &lt;element name="PartialProblems">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PartialProblem" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="TaskId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *                             &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                             &lt;element name="NodeID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
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
@XmlType(name = "", propOrder = {
    "problemType",
    "id",
    "commonData",
    "solvingTimeout",
    "partialProblems"
})
@XmlRootElement(name = "SolvePartialProblems")
public class SolvePartialProblems {

    @XmlElement(name = "ProblemType", required = true)
    protected String problemType;
    @XmlElement(name = "Id", required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger id;
    @XmlElement(name = "CommonData", required = true)
    protected byte[] commonData;
    @XmlElement(name = "SolvingTimeout")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger solvingTimeout;
    @XmlElement(name = "PartialProblems", required = true)
    protected SolvePartialProblems.PartialProblems partialProblems;

    /**
     * Gets the value of the problemType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProblemType() {
        return problemType;
    }

    /**
     * Sets the value of the problemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProblemType(String value) {
        this.problemType = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the commonData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCommonData() {
        return commonData;
    }

    /**
     * Sets the value of the commonData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCommonData(byte[] value) {
        this.commonData = value;
    }

    /**
     * Gets the value of the solvingTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSolvingTimeout() {
        return solvingTimeout;
    }

    /**
     * Sets the value of the solvingTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSolvingTimeout(BigInteger value) {
        this.solvingTimeout = value;
    }

    /**
     * Gets the value of the partialProblems property.
     * 
     * @return
     *     possible object is
     *     {@link SolvePartialProblems.PartialProblems }
     *     
     */
    public SolvePartialProblems.PartialProblems getPartialProblems() {
        return partialProblems;
    }

    /**
     * Sets the value of the partialProblems property.
     * 
     * @param value
     *     allowed object is
     *     {@link SolvePartialProblems.PartialProblems }
     *     
     */
    public void setPartialProblems(SolvePartialProblems.PartialProblems value) {
        this.partialProblems = value;
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
     *         &lt;element name="PartialProblem" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="TaskId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
     *                   &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *                   &lt;element name="NodeID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
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
        "partialProblem"
    })
    public static class PartialProblems {

        @XmlElement(name = "PartialProblem", required = true)
        protected List<SolvePartialProblems.PartialProblems.PartialProblem> partialProblem;

        /**
         * Gets the value of the partialProblem property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the partialProblem property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPartialProblem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SolvePartialProblems.PartialProblems.PartialProblem }
         * 
         * 
         */
        public List<SolvePartialProblems.PartialProblems.PartialProblem> getPartialProblem() {
            if (partialProblem == null) {
                partialProblem = new ArrayList<SolvePartialProblems.PartialProblems.PartialProblem>();
            }
            return this.partialProblem;
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
         *         &lt;element name="TaskId" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
         *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
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
        @XmlType(name = "", propOrder = {
            "taskId",
            "data",
            "nodeID"
        })
        public static class PartialProblem {

            @XmlElement(name = "TaskId", required = true)
            @XmlSchemaType(name = "unsignedLong")
            protected BigInteger taskId;
            @XmlElement(name = "Data", required = true)
            protected byte[] data;
            @XmlElement(name = "NodeID", required = true)
            @XmlSchemaType(name = "unsignedLong")
            protected BigInteger nodeID;

            /**
             * Gets the value of the taskId property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getTaskId() {
                return taskId;
            }

            /**
             * Sets the value of the taskId property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setTaskId(BigInteger value) {
                this.taskId = value;
            }

            /**
             * Gets the value of the data property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getData() {
                return data;
            }

            /**
             * Sets the value of the data property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setData(byte[] value) {
                this.data = value;
            }

            /**
             * Gets the value of the nodeID property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNodeID() {
                return nodeID;
            }

            /**
             * Sets the value of the nodeID property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNodeID(BigInteger value) {
                this.nodeID = value;
            }

        }

    }

}
