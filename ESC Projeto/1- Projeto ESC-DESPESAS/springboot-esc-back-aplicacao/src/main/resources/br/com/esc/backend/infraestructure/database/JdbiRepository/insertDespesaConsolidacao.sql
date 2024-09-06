INSERT INTO tbd_ConsolidacaoDespesasParceladas
    (
    id_Consolidacao,
    id_DespesaParcelada,
    id_Funcionario,
    dt_Associacao
    )
VALUES (
   :despesa.idConsolidacao,
   :despesa.idDespesaParcelada,
   :despesa.idFuncionario,
   CONVERT (date, GETDATE())
)