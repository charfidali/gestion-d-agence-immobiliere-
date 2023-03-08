package pi.app.estatemarket.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.scheduling.annotation.Scheduled;
import pi.app.estatemarket.Entities.Contract;
import pi.app.estatemarket.Repository.ContractRepository;



public class PDFGeneratorService {

	@Autowired
	public static ContractRepository RecRepo;

	@Scheduled(cron = "0 0 0 1 * *")
	public static  ByteArrayInputStream customerPDFReport(List<Contract> listp) {


		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

        	PdfWriter.getInstance(document, out);
            document.open();

			// Add Text to PDF file ->
			Font font = FontFactory.getFont(FontFactory.COURIER_BOLDOBLIQUE, 30, BaseColor.RED);
			Paragraph para = new Paragraph( "Les Contracts du mois Courant", font);

			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);
			document.add(Chunk.NEWLINE);
			Font fontt = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 18, BaseColor.BLACK);
			Paragraph paraa = new Paragraph( "au dessous vous trouvez tous les informations concernant chaque contrat au cours de ce mois", fontt);

			paraa.setAlignment(Element.ALIGN_CENTER);
			document.add(paraa);
			document.add(Chunk.NEWLINE);








        	PdfPTable table = new PdfPTable(4);
        	// Add PDF Table Header ->
			Stream.of("type_contract", "start_date_contract", "end_date_contract","contractActive")
			    .forEach(headerTitle -> {
			          PdfPCell header = new PdfPCell();
			          Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			          header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			          header.setHorizontalAlignment(Element.ALIGN_CENTER);
			          header.setBorderWidth(2);
			          header.setPhrase(new Phrase(headerTitle, headFont));
			          table.addCell(header);
			    });


			   Date currentSqlDate = new Date(System.currentTimeMillis());
			 for (Contract pub: listp	) {

	            if(pub.getStartDateContract().getDay()==(currentSqlDate.getDay())  && pub.getStartDateContract().getMonth()==(currentSqlDate.getMonth())

	            		 && pub.getStartDateContract().getYear()==(currentSqlDate.getYear())	)
	            		 {



	            	PdfPCell date = new PdfPCell(new Phrase(String.valueOf(pub.getTypeContract())));

	            	date.setPaddingLeft(4);
	            	date.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            	date.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(date);

	                PdfPCell date2 = new PdfPCell(new Phrase(String.valueOf(pub.getStartDateContract())));

	            	date.setPaddingLeft(4);
	            	date.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            	date.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(date2);


	                PdfPCell nblikes = new PdfPCell(new Phrase(String.valueOf(pub.getEndDateContract())));
	                nblikes.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                nblikes.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                nblikes.setPaddingRight(4);
	                table.addCell(nblikes);

							 PdfPCell date3 = new PdfPCell(new Phrase(String.valueOf(pub.isContractActive())));
							 nblikes.setVerticalAlignment(Element.ALIGN_MIDDLE);
							 nblikes.setHorizontalAlignment(Element.ALIGN_RIGHT);
							 nblikes.setPaddingRight(4);
							 table.addCell(date3);


	            }


	            }





            document.add(table);

            document.close();





        }catch(DocumentException e) {
        }

		return new ByteArrayInputStream(out.toByteArray());
	}






}