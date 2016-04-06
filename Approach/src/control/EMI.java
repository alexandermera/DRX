package control;

import java.math.BigInteger;
import java.util.HashMap;

/*
 * Matriz EMI
 * 
 * @author Alexander, Bernardo
 */
public class EMI 
{
	private float EMI[ ][ ] ;
	private int matrizCoocorrencia[ ][ ] ;
	private String labels[ ]  ;
	int lsize[ ] ;
	private float mutualinformation ;
	private Double NMI ;
	private Double purity ;
	private float N = 549 ;
	
	public EMI( int[ ][ ] mCoocorrencia , String labels[ ] , int lsize[ ] )
	{
		this.labels =labels ;
		this.lsize =lsize ;
		matrizCoocorrencia = completaMCoocorrencia(mCoocorrencia) ;
		
		EMI = new float [ matrizCoocorrencia.length - 1 ] [ matrizCoocorrencia[ 0 ].length - 1 ] ;
		
		//Inicializando matriz EMI
        for( int x = 0; x < EMI.length; x++ )
        {
        	for( int y = 0; y < EMI[ 0 ].length; y++ )
            {
        		EMI[ x ][ y ] =  0.0f ;
            } /* for */
        } /* for */
	}
	
	private int[][] completaMCoocorrencia(int[][] matrizCoocorrencia ) {
		// TODO Auto-generated method stub
		
		  int maxColuna = -1 ;
	        int inxColuna = -1 ;
	        int maxLinha = -1 ;
	        int inxLinha = -1 ;

	        //Soma das linhas e columnas da matriz de coocorrencia
	        for ( int x = 0; x < matrizCoocorrencia.length; x++ )
	        {
	        	int somaLinha = 0 ;
	             for ( int y = 0; y < matrizCoocorrencia[ x ].length; y++ )
	             {
	            	 if( y == matrizCoocorrencia[ x ].length - 1 )
	            	 {
	            		 matrizCoocorrencia[ x ][ y ] = somaLinha ;
	            		 
	            		 if( maxLinha < somaLinha && x != matrizCoocorrencia.length - 1 )
	            		 {
	            			 maxLinha = somaLinha ;
	            			 inxLinha = x ;
	            		 } /* if */
	            	 }
	            	 else
	            	 {
	            		 somaLinha += matrizCoocorrencia[ x ][ y ] ;
	            	 } /* if */
	            	 
	            	 if( x != matrizCoocorrencia.length - 1 )
	            	 {
	            		 matrizCoocorrencia[ matrizCoocorrencia.length - 1 ][ y ] += matrizCoocorrencia[ x ][ y ] ;
	            	 } /* if */
	            	 
	            	 if( maxColuna < matrizCoocorrencia[ x ][ y ] && y != matrizCoocorrencia[ x ].length - 1 )
	        		 {
	        			 maxColuna = matrizCoocorrencia[ x ][ y ] ;
	        			inxColuna = y ;
	        		 } /* if */                    	 
	                 System.out.print( "       "+ matrizCoocorrencia[ x ][ y ]+"     " ) ;
	             } /* for */
	             System.out.println( ) ;
	        } /* for */
	        
	        return matrizCoocorrencia ;
		}

	public float[ ][ ] execute( )
	{
		float mij , mrs , mis , mrj ;
        mij = mrs = mis = mrj = mutualinformation = 0.000f ;
        mij = matrizCoocorrencia[ matrizCoocorrencia.length - 1 ][ matrizCoocorrencia[ 0 ].length - 1 ] ;
        //mij= N ;
        
       //System.out.println("****************** Matriz EMI **********************") ;
        int i, j = 0 ;
        System.out.print( String.format("%-23s", "content/community") ) ;

		for ( i =0 ; i < EMI[0].length ; i++){
           System.out.print( String.format("%-10s", ""+i) ) ;
		}
		
		System.out.println( ) ;
		
        for ( int r = 0; r < EMI.length; r++ )
        {
           System.out.print( String.format("%-15s", labels[r]) ) ;

            for ( int s = 0 ; s < EMI[ r ].length; s++ )
            {
                mrs = matrizCoocorrencia[ r ] [ s ] ;
                //mis numero de elementos en la clase i
               // mis = matrizCoocorrencia[ r ] [ matrizCoocorrencia[ 0 ].length - 1 ] ;
                mis = lsize[ r ] ;
                //mrj numero de elementos en el cluster
                mrj = matrizCoocorrencia[ matrizCoocorrencia.length - 1 ] [ s ] ;
                
                try 
                {
                	float log = mij * ( mrs /( mrj * mis ) ) ;
                
	                if( mij == 0 )
	            	{
	            		EMI[ r ][ s ] = 0.0f ;
	            	}
	                else if( mrj == 0 || mis == 0 )
                	{
                		EMI[ r ][ s ] = 0.0f ;
                	}
	                else if( log == 0 )
                	{
                		EMI[ r ][ s ] = 0.0f ;
                	}
                	else
                	{
                		EMI[ r ][ s ] = ( float ) ( ( mrs / mij ) * Math.log( mij * ( mrs /( mrj * mis ) ) ) ) ;
                	} /* if */
                } 
                catch ( ArithmeticException e ) 
                {
                    EMI[ r ][ s ] = 0.0f ;
                } /* try */
                
                mutualinformation = mutualinformation + EMI[ r ][ s ]  ;
                System.out.print( String.format("%-10s", " "+String.format("%.3f", EMI[ r ][ s ] ) ) ) ;
             } /* for */
            System.out.println( ) ;
        }
        
       calculatePurity( ) ;
       calculateNMI( ) ;
        return EMI ;
	}
	
	//calculate RI, P, R, F-measure
	private void calculateFM() {
		double fp,tp ,fn,temfn,temfp,precision,recall,fm ;
		precision=recall=fm=fp = tp = fn = temfn = temfp = 0 ;
		HashMap<String, Integer> cluster2domain = new HashMap<String, Integer> ( ) ; 
		for(int l = 0 ; l < labels.length ; l++){
			float max =  0.000f ;
			for (int j = 0; j < EMI[ 0 ].length; j++) {
				if( EMI[ l ] [ j ] > max ){
					max = EMI[ l ] [ j ] ;
					boolean f = false ;
					for ( String domain : cluster2domain.keySet(  ) ) {
						if( cluster2domain.get( domain ) == j)
							f = true ;
					}
					
					if(!f)
						cluster2domain.put( labels[ l ], j ) ;
				}
			}
			
			int r ;
			try{
			if(cluster2domain.containsKey( labels[l] ) ){
				if( cluster2domain.get(labels[l]) > 0)
				for(r = 0; r < EMI.length; r++){
					if ( EMI[ r ] [ cluster2domain.get( labels[l] )] > max){
						cluster2domain.put( labels[ l ], -1 ) ;
						break ;
					}
				}
			}
			}catch(ArrayIndexOutOfBoundsException ex){
				//System.out.println();
			}
		}
		for( String domain:cluster2domain.keySet( ) ){
			if( cluster2domain.get( domain ) >0){
				int l ;
				for( l= 0 ; l < labels.length ; l++){
					if(labels[l].equalsIgnoreCase( domain ) ){
						break ;
					}
				}
				
				tp = tp + matrizCoocorrencia[ l ][ cluster2domain.get( domain ) ] ;
				temfp = matrizCoocorrencia[ l ][ matrizCoocorrencia[ 0 ].length - 1 ] - matrizCoocorrencia[ l ][ cluster2domain.get( domain ) ] ;
				fp = fp + temfp ;
				temfn = matrizCoocorrencia[ matrizCoocorrencia.length - 1 ][ cluster2domain.get( domain ) ] - matrizCoocorrencia[ l ][ cluster2domain.get( domain ) ] ;
				fn = fn + temfn ;
			}
		}
		
		precision 	= tp / ( tp + fp ) ;
		recall		= tp / ( tp + fn ) ;
		fm 			= 2 * ( ( precision * recall )/( precision +  recall ) ) ;
		System.out.print( "P: " + precision + " R: " + recall + " F1: " + fm ) ;
	}

	//normalized mutual information or NMI : 
	private void calculateNMI() {
		float sum , HG , HC , NMI  ,Wk , log  ;
		sum = HG = HC = NMI  = Wk = log = 0.000f ;
		N = matrizCoocorrencia[ matrizCoocorrencia.length - 1 ][ matrizCoocorrencia[ 0 ].length - 1 ] ;
		
		for( int c = 0; c < matrizCoocorrencia[ 0 ].length -1 ; c ++){
			Wk = matrizCoocorrencia[ matrizCoocorrencia.length - 1 ][ c ] ;
			 try{
				 log = (float) Math.log( Wk / N ) ;
			 }catch ( ArithmeticException e ) 
             {
                 log = 0.0f ;
             } /* try */
         
			 if (Float.isInfinite( log ) )
				 	log = 0.0f ;
			sum =  sum +  ( float )  ( Wk  / N ) * log ; 
		}
		
		HG = -sum ;
		
		sum = 0.000f ;
		for( int r = 0; r < matrizCoocorrencia.length -1 ; r ++){
			Wk = matrizCoocorrencia[ r ][ matrizCoocorrencia[ 0 ].length - 1   ] ;
			double logtemp = Wk / N ;
			if ( logtemp != 0)
				sum =  sum +  ( float ) ( ( Wk  / N ) * Math.log( Wk / N ) ) ; 
			if (Double.isNaN(sum))
				System.out.println( );
		}
		
		HC = -sum ;
		NMI = mutualinformation /( Math.abs( HG + HC ) / 2 ) ; 
		this.NMI = (double) NMI ; 
		System.out.println("NMI: " + NMI + " " ) ;
		
	}

	private void calculatePurity( ) {
		float mct , mc , t ;
		mct = mc = t =  0.000f ;

		
		for( int c = 0; c < matrizCoocorrencia[ 0 ].length -1 ; c ++){
			mc = 0 ;
			
			for( int r = 0; r < matrizCoocorrencia.length -1 ; r++ ){
				if ( matrizCoocorrencia[r][c] > mc )
					mc = matrizCoocorrencia[r][c] ;
				//t = t + matrizCoocorrencia[r][c] ;
			}
			mct = mct + mc ; 
		}
		//purity
		this.purity = (double) ( mct / N) ;
		//System.out.print("Purity: " + ( mct / N) + " " ) ;
	}

	public float[ ][ ] getEMI( ) 
	{
		return EMI ;
	}
	public Double getNMI( ) 
	{
		return this.NMI ;
	}

	public void setEMI( float[ ][ ] EMI ) 
	{
		this.EMI = EMI;
	}

	public Double getPurity() {
		// TODO Auto-generated method stub
		return purity;
	}
}