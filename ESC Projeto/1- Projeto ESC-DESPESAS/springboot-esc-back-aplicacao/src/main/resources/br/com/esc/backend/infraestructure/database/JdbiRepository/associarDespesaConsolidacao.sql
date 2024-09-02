INSERT INTO tbd_ConsolidacaoDespesasParceladas
    (
    id_Consolidacao,
    id_DespesaParcelada,
    nr_ParcelasAdiantadas,
    tp_Baixado,
    id_Funcionario,
    dt_Associacao
    )
VALUES (
   :despesa.idConsolidacao,
   :despesa.idDespesaParcelada,
   :despesa.nrParcelasAdiantadas,
   'N',
   :despesa.idFuncionario,
   CONVERT (date, GETDATE())
)