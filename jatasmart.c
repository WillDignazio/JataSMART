#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include <jni.h>
#include <atasmart.h>
#include "JataSMART.h"
#include "JataSMART_SkDisk.h"

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
	case EIO:
	case ENODEV:
		jobj = (*env)->FindClass(env, "java/io/IOException");
		break;
	default:
		jobj = (*env)->FindClass(env, "java/lang/RuntimeException");
	}

	return (*env)->ThrowNew(env, jobj, msg);
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
	if(jobj == NULL)
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

JNIEXPORT void JNICALL
Java_JataSMART_hello(JNIEnv *env, jobject this)
{
	printf("Hello!");
}
