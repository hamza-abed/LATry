package shared.pck;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;



/**
 * Permet de construire plus facilement des packets et de les lires sous DarkStar.
 * Ne connais pas la structure du packet... elle doit Ãªtre gerer allieurd
 * 
 * <ul>
 * <li>Le premier element du packet est son code. Qui est un short</li>
 * <li>Les chaines sont prÃ©cÃ©dÃ© d'un short indiquand leurs tailles</li>
 * <li>Les chaines sont encodÃ© par defaut en UTF-8</li>
 * <li></li>
 * </ul>
 * 
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */


public class Pck {
	private static final Logger logger = Logger.getLogger(Pck.class.getName());

	/**
	 * forÃ¹at d'encodage des chaines
	 */
	private static final String STRING_ENCODING = "UTF-8";


	/**
	 * Liste des objet contenu dans le packet
	 */
	private ArrayList<Object> datas = new ArrayList<Object>();

	/**
	 * Taille d'un packet en octet
	 */
	private int len = 0;

	/**
	 * Constructeur de Pck
	 */
	public Pck(short pckCode) {
		putShort(pckCode);
	}

	/* ************************************************************** *
	 * *					Ajout d'element							* * 
	 * ************************************************************** */
	/**
	 * Ajout des shorts au packet.
	 * @param shorts
	 */
	public Pck putShort(short... shorts) {
		logger.entering(Pck.class.getName(),"putShort");

		for (short s : shorts) {
			len += Short.SIZE / Byte.SIZE;
			datas.add(s);
		}

		logger.exiting(Pck.class.getName(),"putShort");
		return this;
	}

	/**
	 * Ajout d'integer au packet.
	 * @param ints
	 * @return 
	 */
	public Pck putInt(int... ints) {
		logger.entering(Pck.class.getName(),"putInt");

		for (int i : ints) {
			len += Integer.SIZE / Byte.SIZE;
			datas.add(i);
		}

		logger.exiting(Pck.class.getName(),"putInt");
		return this;
	}

	/**
	 * Ajout de byte au packet.
	 * @param bytes
	 */
	public Pck putByte(byte... bytes) {
		logger.entering(Pck.class.getName(),"putByte");

		for (byte b : bytes) {
			len += Byte.SIZE / Byte.SIZE;
			datas.add(b);
		}

		logger.exiting(Pck.class.getName(),"putByte");
		return this;
	}

	/**
	 * Ajout de floattants au packet.
	 * @param floats
	 * @return 
	 */
	public Pck putFloat(float... floats) {
		logger.entering(Pck.class.getName(),"putFloat");

		for (float f : floats) {
			len += Float.SIZE / Byte.SIZE;
			datas.add(f);
		}

		logger.exiting(Pck.class.getName(),"putFloat");
		return this;
	}

	/**
	 * Ajout de chaine au packet.
	 * @param strings
	 * @return 
	 */
	public Pck putString(String... strings) {
		logger.entering(Pck.class.getName(),"putString");

		for (String s : strings) try {
			if (s == null) {
				putShort((short)0);
			} else {
				byte[] str = s.getBytes(Pck.STRING_ENCODING);
				putShort((short)str.length);
				len += str.length;
				datas.add(str);
			}
		} catch (UnsupportedEncodingException e) {
			logger.warning("UnsupportedEncodingException : le systeme ne supporte pas : "+Pck.STRING_ENCODING);
		}

		logger.exiting(Pck.class.getName(),"putString");
		return this;
	}
	
	/**
	 * ajout de booleans
	 * @param bools
	 * @return
	 */
	public Pck putBoolean(boolean... bools) {
		logger.entering(Pck.class.getName(),"putString");

		for (boolean b : bools) 
		 putByte(b?(byte)1:(byte)0);

		logger.exiting(Pck.class.getName(),"putString");
		return this;
	}

	/**
	 * Ajout une valeur d'un type enumÃ©re
	 * @param value
	 * @return 
	 */
	public Pck putEnum(Enum<?> value) {
		return putByte((byte)value.ordinal());
	}
	
	
	/* ************************************************************** *
	 * *				Lectur d'objet non primitif					* * 
	 * ************************************************************** */
	
	/**
	 * lit une chaine depuis le packet sous forme d'un bytebuffer
	 * @param message original
	 */
	public static String readString(ByteBuffer message) {
		short l = message.getShort();
		if (l == 0)	return null;
		byte[] bytes = new byte[l];
		message.get(bytes);
		try {
			return new String(bytes,STRING_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.warning("UnsupportedEncodingException : le systeme ne supporte pas : "+Pck.STRING_ENCODING);
			return new String(bytes);
		}
	}
	
	/**
	 * Lit l'ordinal d'un type enumerÃ© (je ne vois pas comment faire autrement)
	 * @param message
	 * @return
	 */
	public static int readEnumOrdinal(ByteBuffer message) {
		return message.get();
	}
	
	/**
	 * Permet de lire un type enumÃ©rÃ©
	 * @param <EnumType>
	 * @param enumType
	 * @param message
	 * @return
	 */
	public static <EnumType extends Enum<?>> EnumType readEnum(Class<EnumType> enumType, ByteBuffer message) {
		return enumType.getEnumConstants()[readEnumOrdinal(message)];
	}
	
	/**
	 * lit un boolean
	 */
	public static boolean readBoolean(ByteBuffer message) {
		return message.get()==1;
	}
	
	
	/* ************************************************************** *
	 * *					Transphormation							* * 
	 * ************************************************************** */
	
	/**
	 * transphorme le packet en byte buffer pour Ãªtre ballancÃ© sur le reseau
	 */
	public ByteBuffer toByteBuffer() {
		logger.entering(Pck.class.getName(),"toByteBuffer");
		ByteBuffer buffer = ByteBuffer.allocate(this.len);
		
		for (Object obj : datas) 
			if (obj instanceof Integer) 
				buffer.putInt((Integer)obj);
			else if (obj instanceof Short)
				buffer.putShort((Short)obj);
			else if (obj instanceof Float)
				buffer.putFloat((Float)obj);
			else if (obj instanceof Byte)
				buffer.put((Byte)obj);
			else if (obj instanceof byte[])
				buffer.put((byte[])obj);
			else 
				logger.warning("Un type de donnÃ©e inconnu a Ã©tÃ© rencontrÃ© dans le packet.");
		
		buffer.flip();
		
		logger.exiting(Pck.class.getName(),"toByteBuffer");
		return buffer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		logger.entering(Pck.class.getName(),"toString");
		String str = "";
		
		for (Object object : this.datas) 
			if (object instanceof byte[])
				try {
					str+=new String((byte[])object,STRING_ENCODING) +"\n";
				} catch (UnsupportedEncodingException e) {
					logger.warning("Erreur le systeme ne supporte pas l'encodage "+STRING_ENCODING+" : WTF !!!");
				}
			else 
				str += object.toString()+"\n";
		
		logger.exiting(Pck.class.getName(),"toString");
		return str;
	}

}
