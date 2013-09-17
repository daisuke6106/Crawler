package jp.co.dk.crawler.dao.record;

import java.util.Date;
import java.util.Map;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * PagesRecord�́APAGES�e�[�u���̒P��̃��R�[�h��\���N���X�B
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class PagesRecord implements DataConvertable{
	
	/** �v���g�R�� */
	protected String protocol;
	
	/** �z�X�g�� */
	protected String host;
	
	/** �p�X */
	protected String path;
	
	/** �p�����[�^ */
	protected Map<String, String> parameter; 
	
	/** ���N�G�X�g�w�b�_�[ */
	protected Map<String, String> requestHeader;
	
	/** ���X�|���X�w�b�_�[ */
	protected Map<String, String> responceHeader;
	
	/** �R���e���c */
	protected byte[] contents;
	
	/** �쐬���� */
	protected Date createDate;
	
	/** �X�V���� */
	protected Date updateDate;
	
	@Override
	public DataConvertable convert(DataBaseRecord arg0)	throws DataStoreManagerException {
		return null;
	}

	@Override
	public DataConvertable convert(Record arg0)	throws DataStoreManagerException {
		return null;
	}

}
