package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Directory {
    protected var children: List<Child>? = null
    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     * [String]
     */
    var id: String? = null
    /**
     * Gets the value of the parentId property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the parentId property.
     *
     * @param value allowed object is
     * [String]
     */
    var parentId: String? = null
    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     * [String]
     */
    var name: String? = null
    /**
     * Gets the value of the starred property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the starred property.
     *
     * @param value allowed object is
     * [String]
     */
    var starred: LocalDateTime? = null
    /**
     * Gets the value of the userRating property.
     *
     * @return possible object is
     * [Integer]
     */
    /**
     * Sets the value of the userRating property.
     *
     * @param value allowed object is
     * [Integer]
     */
    var userRating: Int? = null
    /**
     * Gets the value of the averageRating property.
     *
     * @return possible object is
     * [Double]
     */
    /**
     * Sets the value of the averageRating property.
     *
     * @param value allowed object is
     * [Double]
     */
    var averageRating: Double? = null
    /**
     * Gets the value of the playCount property.
     *
     * @return possible object is
     * [Long]
     */
    /**
     * Sets the value of the playCount property.
     *
     * @param value allowed object is
     * [Long]
     */
    var playCount: Long? = null

    /**
     * Gets the value of the children property.
     *
     *
     *
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the children property.
     *
     *
     *
     * For example, to add a new item, do as follows:
     * <pre>
     * getchildren().add(newItem);
    </pre> *
     *
     *
     *
     *
     * Objects of the following type(s) are allowed in the list
     * [Child]
     */
    fun getchildren(): List<Child>? {
        if (children == null) {
            children = ArrayList()
        }
        return children
    }
}