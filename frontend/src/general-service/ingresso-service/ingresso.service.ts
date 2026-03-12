import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

@Injectable({
  providedIn: 'root'
})
export class IngressoService {

  async gerarPDF(dados: any) {
    const vouchers = dados.vouchers || dados.ingressosIds || [];
    const pdf = new jsPDF('p', 'mm', 'a4');
    const pdfWidth = pdf.internal.pageSize.getWidth();

    for (let i = 0; i < vouchers.length; i++) {
      const div = document.createElement('div');
      div.style.position = 'absolute';
      div.style.left = '-9999px';
      div.style.width = '550px'; 
      div.innerHTML = this.getSingleTicketTemplate(dados, i);
      document.body.appendChild(div);

      try {
        const canvas = await html2canvas(div, { scale: 2, useCORS: true });
        const imgData = canvas.toDataURL('image/png');
        const imgProps = pdf.getImageProperties(imgData);
        const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

        if (i > 0) {
          pdf.addPage();
        }

        pdf.addImage(imgData, 'PNG', 0, 10, pdfWidth, pdfHeight);
      } finally {
        document.body.removeChild(div);
      }
    }

    pdf.save(`Ingressos_${dados.filmeTitulo || 'Cinema'}.pdf`);
  }

  private getSingleTicketTemplate(dados: any, index: number): string {
    const vouchers = dados.vouchers || dados.ingressosIds || [];
    const assentos = dados.assentosCodigos || [];
    const voucher = vouchers[index];
    const assento = assentos[index] || 'N/A';

    return `
      <div style="width: 550px; background: white; border-radius: 12px; overflow: hidden; font-family: sans-serif; border: 2px solid #eee;">
        <div style="display: flex;">
          <div style="background: #c91432; width: 50px; display: flex; align-items: center; justify-content: center;">
            <h2 style="color: white; transform: rotate(-90deg); white-space: nowrap; font-size: 20px; letter-spacing: 4px; margin: 0;">CINE APP</h2>
          </div>

          <div style="flex: 1; padding: 25px;">
            <div style="display: flex; justify-content: space-between; border-bottom: 2px solid #f4f4f4; margin-bottom: 15px; padding-bottom: 10px;">
              <h2 style="margin: 0; color: #333; font-size: 22px;">${dados.filmeTitulo}</h2>
              <span style="background: #ffc107; color: #000; padding: 4px 10px; border-radius: 5px; font-size: 10px; font-weight: bold; height: fit-content;">${index + 1}/${vouchers.length}</span>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
              <div>
                <small style="color: #999; text-transform: uppercase; font-weight: bold; font-size: 10px;">Sala</small>
                <div style="font-weight: bold; color: #333;">${dados.salaNome}</div>
              </div>
              <div>
                <small style="color: #999; text-transform: uppercase; font-weight: bold; font-size: 10px;">Assento</small>
                <div style="font-weight: bold; color: #c91432; font-size: 20px;">${assento}</div>
              </div>
              <div>
                <small style="color: #999; text-transform: uppercase; font-weight: bold; font-size: 10px;">Data</small>
                <div style="font-weight: bold;">${dados.data}</div>
              </div>
              <div>
                <small style="color: #999; text-transform: uppercase; font-weight: bold; font-size: 10px;">Horário</small>
                <div style="font-weight: bold;">${dados.horario}</div>
              </div>
            </div>

            <div style="margin-top: 25px; padding: 15px; background: #fffbef; border: 2px dashed #ffc107; border-radius: 8px; text-align: center;">
              <div style="color: #666; font-size: 10px; font-weight: bold; margin-bottom: 5px;">VOUCHER DE ACESSO</div>
              <div style="font-family: monospace; font-size: 22px; color: #c91432; font-weight: bold; letter-spacing: 2px;">
                ${voucher}
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
  }
}