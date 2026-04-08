package android.net

import android.os.Parcel

/**
 * Requires `testOptions.unitTests.isReturnDefaultValues = true`
 */
object FakeUri : Uri() {
    override fun compareTo(other: Uri?): Int = 0
    override fun toString(): String = "fake://uri"
    override fun isAbsolute(): Boolean = true
    override fun isRelative(): Boolean = false
    override fun isOpaque(): Boolean = false
    override fun isHierarchical(): Boolean = true
    override fun getScheme(): String = "fake"
    override fun getSchemeSpecificPart(): String = "//uri"
    override fun getEncodedSchemeSpecificPart(): String = "//uri"
    override fun getAuthority(): String? = null
    override fun getEncodedAuthority(): String? = null
    override fun getUserInfo(): String? = null
    override fun getEncodedUserInfo(): String? = null
    override fun getHost(): String? = null
    override fun getPort(): Int = -1
    override fun getPath(): String = ""
    override fun getEncodedPath(): String = ""
    override fun getQuery(): String? = null
    override fun getEncodedQuery(): String? = null
    override fun getFragment(): String? = null
    override fun getEncodedFragment(): String? = null
    override fun getPathSegments(): List<String> = emptyList()
    override fun getLastPathSegment(): String? = null
    override fun buildUpon(): Builder = Builder()
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}
}
