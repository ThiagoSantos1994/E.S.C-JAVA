UPDATE
    tbd_DespesasFixasMensais
SET
    ds_Descricao = UPPER(:fixas.dsDescricao),
    vl_Total = :fixas.vlTotal,
    tp_Status = :fixas.tpStatus,
    tpFixasObrigatorias = :fixas.tpFixasObrigatorias,
    tp_DespesaDebitoCartao = :fixas.tpDespesaDebitoCartao
WHERE
   id_Despesa = :fixas.idDespesa
   AND id_Ordem = :fixas.idOrdem
   AND id_Funcionario = :fixas.idFuncionario
   AND ds_Mes = :fixas.dsMes
   AND ds_Ano = :fixas.dsAno
