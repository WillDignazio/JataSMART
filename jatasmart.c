#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include <jni.h>
#include <atasmart.h>
#include "JataSMART.h"
#include "JataSMART_SkDisk.h"

#define JSKERR	0xDEADBEEF

/* Wrapper to throw the SkException */
jint
throwSkException(JNIEnv *env, int err, const char *msg)
{
	char *errstr;
	jobject jobj;
	int ret;

	jobj = NULL;
	ret = -1;

	switch(err)
	{
	case ENOMEM:
		jobj = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
		break;
	case ENOENT:
		jobj = (*env)->FindClass(env, "java/lang/FileNotFoundException");
		break;
	case ENOTSUP:
		jobj = (*env)->FindClass(env, "java/lang/UnsupportedOperationException");
		break;
	case EIO:
	case ENODEV:
		jobj = (*env)->FindClass(env, "java/io/IOException");
		break;
	default:
		jobj = (*env)->FindClass(env, "java/lang/RuntimeException");
	}

	return (*env)->ThrowNew(env, jobj, msg);
}

/* Get the fieldID for the Java SkDisk Object address field */
static jfieldID
_getskdiskid(JNIEnv *env, jobject jskdisk)
{
	jfieldID jaddrid;
	jobject cls;

	cls = (*env)->FindClass(env, "JataSMART$SkDisk");
	if(cls == NULL)
		goto fail;

	jaddrid = (*env)->GetFieldID(env, cls, "_skDiskAddr", "J");
	if((*env)->GetLongField(env, jskdisk, jaddrid) == JSKERR)
		throwSkException(env, -1, "Uninitialized SkDisk Object");

fail:
	return jaddrid;
}

/* Set the long address field for the C SkDisk pointer */
static void
setskdisk(JNIEnv *env, jobject jskdisk, SkDisk *skdisk)
{
	jfieldID jaddrid;

	jaddrid = _getskdiskid(env, jskdisk);
	if(jaddrid == NULL)
		return;

	(*env)->SetLongField(env, jskdisk, jaddrid, (long)skdisk);
}

/* Get the long address field fot he C SkDisk pointer */
static SkDisk*
getskdisk(JNIEnv *env, jobject jskdisk)
{
	jfieldID jaddrid;
	SkDisk *skdisk;

	jaddrid = _getskdiskid(env, jskdisk);
	if(jaddrid == NULL)
		return NULL;

	skdisk = (SkDisk*)(*env)->GetLongField(env, jskdisk, jaddrid);
	return skdisk;
}

JNIEXPORT jboolean JNICALL
Java_JataSMART_00024SkDisk_smartAvailable(JNIEnv *env, jobject this)
{
	SkDisk *disk;
	SkBool avail;
	int ret;

	printf("Getting smart disk information\n");
	disk = getskdisk(env, this);
	if(disk == NULL)
		return JNI_FALSE;

	ret = sk_disk_smart_is_available(disk, &avail);
	if(ret == -1) {
		throwSkException(env, errno, strerror(errno));
		return JNI_FALSE;
	}

	return (avail == TRUE) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jobject JNICALL
Java_JataSMART_open(JNIEnv *env, jobject this, jstring jpath)
{
	jobject jobj;
	jmethodID constructor;
	jfieldID jaddrid;
	jobject cls;
	const char *nstring;
	SkDisk *skdisk;
	int ret;

	jaddrid = NULL;
	skdisk = NULL;
	
	/* XXX Handle class build failure */
	cls = (*env)->FindClass(env, "JataSMART$SkDisk");
	if(cls == NULL)
		goto fail;
	
	constructor = (*env)->GetMethodID(env, cls, "<init>", "(LJataSMART;)V");
	if(constructor == NULL)
		goto fail;
		
	jobj = (*env)->NewObject(env, cls, constructor, this, 10);
	if(jobj == NULL)
		goto fail;

	/* Build atasmart library objects */
	nstring = (*env)->GetStringUTFChars(env, jpath, JNI_FALSE);
	ret = sk_disk_open(nstring, &skdisk);
	if(ret != 0) {
		throwSkException(env, errno, strerror(errno));
		goto fail;
	}

	jaddrid = (*env)->GetFieldID(env, cls, "_skDiskAddr", "J");
	(*env)->SetLongField(env, jobj, jaddrid, (long)&skdisk);

fail:
	return jobj;
}
