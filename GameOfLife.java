

public class GameOfLife extends CAtoolbox {


	public static void main(String[] args) {

		int dx = 50;
		int dy = 50;

		boolean[][] tableauCourant = new boolean[dx][dy];
		boolean[][] nouveauTableau = new boolean[dx][dy];

		int taillePopInit = 500;

		int delai = 100;//100;

		int nombreDePasMaximum = 10000;//1000;
		int it = 0;

		double tauxPerturbation = 0;//0.00005;

		// on initialise la premiere ligne

		for ( int i = 0 ; i != taillePopInit; i++ )
			tableauCourant[(int)(Math.random()*(double)dx)][(int)(Math.random()*(double)dy)] = true;

		// on ajoute un planeur pour tester
		tableauCourant[10][10] = true;
		tableauCourant[10][12] = true;
		tableauCourant[11][12] = true;
		tableauCourant[11][11] = true;
		tableauCourant[12][11] = true;


		// optionnel: initialise la visualisation dans une fenetre

		CAImageBuffer image = new CAImageBuffer(dx,dy);
	    ImageFrame imageFrame =	ImageFrame.makeFrame( "Game Of Life [conway, ~1970]", image, delai, 200, 200 );


		// on fait tourner l'automate

		while ( it != nombreDePasMaximum )
		{
			// 1a - affiche de l'etat courant dans la fenetre graphique (toutes les cellules)
			image.update(tableauCourant);

			// 1 - mise a jour de l'automate (dans le tableau en tampon)
			for ( int x = 0 ; x != tableauCourant.length ; x++ )
				for ( int y = 0 ; y != tableauCourant[0].length ; y++ )
				{
					// met a jour l'automate cellulaire (pour la prochaine etape)
					int cpt = 0;
					for ( int x2 = x-1 ; x2 != x+2 ; x2++ )
						for ( int y2 = y-1 ; y2 != y+2 ; y2++ )
						{
							if ( y2 != y || x2 != x) // ne considere pas la cellule concernee
								if ( x2 >= 0 && x2 < dx && y2 >= 0 && y2 < dy ) // case: inside envt
								{
									if ( tableauCourant[x2][y2] )
										cpt++;
								}
								else // case: at the border.
								{
									if ( tableauCourant[(x2+dx)%dx][(y2+dy)%dy] )
										cpt++;
								}
						}
					if ( Math.random() < tauxPerturbation ) // random perturbation
						nouveauTableau[x][y] = !tableauCourant[x][y];
					else
						if ( cpt < 2 || cpt > 3 ) // optim: parfois ecrasement inutile
							nouveauTableau[x][y] = false;
						else
							if ( ( cpt == 2 || cpt == 3 ) && tableauCourant[x][y] )
							{
								nouveauTableau[x][y] = true;
							}
							else
								if ( cpt == 3 && !tableauCourant[x][y] )
								{
									nouveauTableau[x][y] = true;
								}
								else
									nouveauTableau[x][y] = false;
				}

			// 2 - met a jour le tableau affichable

			for ( int x = 0 ; x != tableauCourant.length ; x++ )
				for ( int y = 0 ; y != tableauCourant[0].length ; y++ )
					tableauCourant[x][y] = nouveauTableau[x][y];

			it++;

			// ne va pas trop vite...

			try {
				Thread.sleep(delai);
			} catch (InterruptedException e)
			{
			}
		}

	}

}
